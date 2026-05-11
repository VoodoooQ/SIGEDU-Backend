package com.gestion.educativa.identidad.identidad.services;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.gestion.educativa.identidad.identidad.config.JwtConfig;
import com.gestion.educativa.identidad.identidad.exceptions.RecursoNoEncontradoException;
import com.gestion.educativa.identidad.identidad.models.entity.Rol;
import com.gestion.educativa.identidad.identidad.models.entity.Usuario;
import com.gestion.educativa.identidad.identidad.models.entity.UsuarioRol;
import com.gestion.educativa.identidad.identidad.models.request.LoginRequest;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional(readOnly = true)
    public String login(LoginRequest solicitud) {
        Usuario usuario = usuarioRepository.findById(solicitud.getRunUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

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
                        .collect(Collectors.toList());

        return jwtConfig.generarToken(usuario.getRunUsuario(), roles);
    }
}
