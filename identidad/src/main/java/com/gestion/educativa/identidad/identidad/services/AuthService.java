package com.gestion.educativa.identidad.identidad.services;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.gestion.educativa.identidad.identidad.config.JwtConfig;
import com.gestion.educativa.identidad.identidad.models.dto.LoginResponse;
import com.gestion.educativa.identidad.identidad.models.entity.Rol;
import com.gestion.educativa.identidad.identidad.models.entity.Usuario;
import com.gestion.educativa.identidad.identidad.models.entity.UsuarioRol;
import com.gestion.educativa.identidad.identidad.models.request.LoginRequest;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    @Value("${jwt.expiracion}")
    private long expiracionJwt;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest solicitud) {
        Usuario usuario = usuarioRepository.findById(solicitud.getRunUsuario())
                .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(solicitud.getContrasena(), usuario.getContrasena())) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        List<String> roles = usuario.getRoles() == null
                ? List.of()
                : usuario.getRoles().stream()
                        .map(UsuarioRol::getRol)
                        .filter(Objects::nonNull)
                        .map(Rol::getNombreRol)
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(nombreRol -> !nombreRol.isBlank())
                        .collect(Collectors.toList());

        String token = jwtConfig.generarToken(usuario.getRunUsuario(), roles);
        long expiraEn = System.currentTimeMillis() + expiracionJwt;
        return new LoginResponse(token, "Bearer", usuario.getRunUsuario(), roles, expiraEn);
    }
}
