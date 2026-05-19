package com.gestion.educativa.identidad.identidad.services;

import java.util.Optional;
import com.gestion.educativa.identidad.identidad.config.JwtTokenConfig;
import com.gestion.educativa.identidad.identidad.models.entity.Usuario;
import com.gestion.educativa.identidad.identidad.models.request.LoginRequest;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenConfig jwtConfig;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginUsuarioInexistenteRetornaCredencialesInvalidas() {
        LoginRequest solicitud = new LoginRequest("99999999", "clave");
        when(usuarioRepository.findById("99999999")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> authService.login(solicitud));
    }

    @Test
    void loginContrasenaIncorrectaRetornaCredencialesInvalidas() {
        Usuario usuario = new Usuario();
        usuario.setRunUsuario("12345678");
        usuario.setContrasena("hash");

        LoginRequest solicitud = new LoginRequest("12345678", "clave-mala");
        when(usuarioRepository.findById("12345678")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("clave-mala", "hash")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.login(solicitud));
    }
}
