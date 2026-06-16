param(
    [string]$RequestFile = 'E:\SIGEDU-Backend\requests.rest'
)

$raw = Get-Content -Path $RequestFile -Raw
$vars = @{}
foreach ($m in [regex]::Matches($raw, '(?m)^@([A-Za-z0-9_]+)\s*=\s*(.+)$')) {
    $vars[$m.Groups[1].Value] = $m.Groups[2].Value.Trim()
}

function Resolve-Template([string]$text, [hashtable]$table) {
    if ($null -eq $text) { return $text }
    return ([regex]::Replace($text, '\{\{([^}]+)\}\}', {
        param($match)
        $key = $match.Groups[1].Value.Trim()
        if ($table.ContainsKey($key)) { return [string]$table[$key] }
        return $match.Value
    }))
}

function Invoke-Request([string]$method, [string]$url, [hashtable]$headers, [string]$body) {
    try {
        $params = @{
            Method = $method
            Uri = $url
            UseBasicParsing = $true
            Headers = $headers
            ErrorAction = 'Stop'
        }
        if ($null -ne $body -and $body.Trim() -ne '') {
            $params['Body'] = $body
            if ($headers.ContainsKey('Content-Type')) {
                $params['ContentType'] = $headers['Content-Type']
                $headers.Remove('Content-Type') | Out-Null
            }
        }
        $resp = Invoke-WebRequest @params
        return [pscustomobject]@{
            Status = [int]$resp.StatusCode
            Success = $true
            Body = [string]$resp.Content
        }
    } catch {
        if ($_.Exception.Response) {
            $response = $_.Exception.Response
            $stream = $response.GetResponseStream()
            $reader = New-Object System.IO.StreamReader($stream)
            $content = $reader.ReadToEnd()
            return [pscustomobject]@{
                Status = [int]$response.StatusCode.value__
                Success = $false
                Body = $content
            }
        }
        return [pscustomobject]@{
            Status = -1
            Success = $false
            Body = $_.Exception.Message
        }
    }
}

$sections = $raw -split '(?m)^### '
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
    if ($method -in @('GET','DELETE')) {
        $body = $null
    }

    $headersClone = @{}
    foreach ($k in $headers.Keys) { $headersClone[$k] = $headers[$k] }
    $res = Invoke-Request -method $method -url $url -headers $headersClone -body $body

    if ($title -like 'MS0 - Login*' -and $res.Success) {
        try {
            $json = $res.Body | ConvertFrom-Json
            if ($json.token) { $vars['token'] = $json.token }
        } catch {}
    }

    $results.Add([pscustomobject]@{
        Title = $title
        Status = $res.Status
        Success = $res.Success
        Url = $url
        Body = $res.Body
    }) | Out-Null
}

$results | ConvertTo-Json -Depth 4
