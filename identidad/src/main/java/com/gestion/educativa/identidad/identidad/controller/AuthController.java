package com.gestion.educativa.identidad.identidad.controller;

import com.gestion.educativa.identidad.identidad.models.dto.LoginResponse;
import com.gestion.educativa.identidad.identidad.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.identidad.identidad.models.request.LoginRequest;
import com.gestion.educativa.identidad.identidad.config.JwtTokenConfig;
import com.gestion.educativa.identidad.identidad.services.AuthService;
import com.gestion.educativa.identidad.identidad.services.UsuarioDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Autenticación")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenConfig jwtConfig;
    private final UsuarioDetailsService usuarioDetailsService;

    @PostMapping("/login")
    @Operation(summary = "Login de usuario", description = "Retorna JWT Bearer token")
    @ApiResponse(responseCode = "200", description = "Login exitoso")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest solicitud) {
        LoginResponse respuesta = authService.login(solicitud);
        return ResponseEntity.ok(respuesta);
    }

@GetMapping("/validar")
@Operation(summary = "Validar token JWT", description = "Usado por otros microservicios para validar token")
@ApiResponse(responseCode = "200", description = "Token valido")
@ApiResponse(responseCode = "401", description = "Token invalido")
public ResponseEntity<?> validarToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body("Token requerido");
    }

    String token = authHeader.substring(7);

    try {
        if (!jwtConfig.validarToken(token)) {
            return ResponseEntity.status(401).body("Token invalido");
        }

        String runUsuario = jwtConfig.obtenerRunDesdeToken(token);
        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(runUsuario);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UsuarioValidadoDto(runUsuario, roles));
    } catch (Exception e) {
        return ResponseEntity.status(401).body("Token invalido o expirado");
    }
}

}
