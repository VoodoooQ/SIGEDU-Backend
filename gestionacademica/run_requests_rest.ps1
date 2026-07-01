param(
    [string]$RequestFile = 'E:\SIGEDU-Backend\requests.rest'
)

Add-Type -AssemblyName System.Net.Http

$raw = Get-Content -Path $RequestFile -Raw
$vars = @{}
foreach ($m in [regex]::Matches($raw, '(?m)^@([A-Za-z0-9_]+)\s*=\s*(.+)$')) {
    $vars[$m.Groups[1].Value] = $m.Groups[2].Value.Trim()
}

function Resolve-Template([string]$text, [hashtable]$vars) {
    if ($null -eq $text) { return $text }
    return ([regex]::Replace($text, '\{\{([^}]+)\}\}', {
        param($match)
        $key = $match.Groups[1].Value.Trim()
        if ($vars.ContainsKey($key)) { return [string]$vars[$key] }
        return $match.Value
    }))
}

$sections = $raw -split '(?m)^### '
$client = New-Object System.Net.Http.HttpClient
$results = New-Object System.Collections.Generic.List[object]

foreach ($section in $sections) {
    if ([string]::IsNullOrWhiteSpace($section)) { continue }
    $lines = $section -split "`r?`n"
    $title = $lines[0].Trim()
    $idx = 1
    while ($idx -lt $lines.Length -and [string]::IsNullOrWhiteSpace($lines[$idx])) { $idx++ }
    if ($idx -ge $lines.Length) { continue }

    $requestLine = $lines[$idx].Trim()
    if ($requestLine -notmatch '^(GET|POST|PUT|DELETE|PATCH)\s+(.+)$') { continue }
    $method = $matches[1]
    $url = Resolve-Template $matches[2].Trim() $vars
    $idx++

    $headers = @{}
    while ($idx -lt $lines.Length -and -not [string]::IsNullOrWhiteSpace($lines[$idx])) {
        if ($lines[$idx] -match '^([^:]+):\s*(.+)$') {
            $headers[$matches[1].Trim()] = Resolve-Template $matches[2].Trim() $vars
        }
        $idx++
    }

    while ($idx -lt $lines.Length -and [string]::IsNullOrWhiteSpace($lines[$idx])) { $idx++ }
    $body = $null
    if ($idx -lt $lines.Length) {
        $bodyText = ($lines[$idx..($lines.Length-1)] -join "`n").Trim()
        if (-not [string]::IsNullOrWhiteSpace($bodyText)) {
            $body = Resolve-Template $bodyText $vars
        }
    }

    try {
        $httpMethod = New-Object System.Net.Http.HttpMethod($method)
        $request = New-Object System.Net.Http.HttpRequestMessage($httpMethod, $url)

        if ($body -ne $null) {
            $mediaType = if ($headers.ContainsKey('Content-Type')) { $headers['Content-Type'] } else { 'application/json' }
            $request.Content = New-Object System.Net.Http.StringContent($body, [System.Text.Encoding]::UTF8, $mediaType)
        }

        foreach ($k in $headers.Keys) {
            if (-not $request.Headers.TryAddWithoutValidation($k, $headers[$k])) {
                if ($request.Content -ne $null) {
                    $request.Content.Headers.Remove($k) | Out-Null
                    $request.Content.Headers.TryAddWithoutValidation($k, $headers[$k]) | Out-Null
                }
            }
        }

        $response = $client.SendAsync($request).Result
        $content = if ($response.Content -ne $null) { $response.Content.ReadAsStringAsync().Result } else { '' }
        $status = [int]$response.StatusCode

        if ($title -like 'MS0 - Login*' -and $status -ge 200 -and $status -lt 300) {
            try {
                $json = $content | ConvertFrom-Json
                if ($json.token) { $vars['token'] = $json.token }
            } catch {}
        }

        $snippet = if ($content.Length -gt 220) { $content.Substring(0,220) } else { $content }
        $results.Add([pscustomobject]@{
            Title = $title
            Method = $method
            Url = $url
            Status = $status
            Success = $response.IsSuccessStatusCode
            Snippet = $snippet
        }) | Out-Null
    }
    catch {
        $results.Add([pscustomobject]@{
            Title = $title
            Method = $method
            Url = $url
            Status = -1
            Success = $false
            Snippet = $_.Exception.Message
        }) | Out-Null
    }
}

$results | ConvertTo-Json -Depth 4
