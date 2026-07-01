package com.gestion.educativa.identidad.identidad.controller;

import com.gestion.educativa.identidad.identidad.config.JwtTokenConfig;
import com.gestion.educativa.identidad.identidad.services.AuthService;
import com.gestion.educativa.identidad.identidad.services.UsuarioDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerValidarTokenTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtTokenConfig jwtConfig;

    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @InjectMocks
    private AuthController authController;

    @Test
    void validarTokenRetorna200ConTokenValido() {
        String token = "token-valido";
        String runUsuario = "12345678";
        UserDetails userDetails = new User(runUsuario, "N/A",
                List.of(new SimpleGrantedAuthority("ADMIN")));

        when(jwtConfig.validarToken(token)).thenReturn(true);
        when(jwtConfig.obtenerRunDesdeToken(token)).thenReturn(runUsuario);
        when(usuarioDetailsService.loadUserByUsername(runUsuario)).thenReturn(userDetails);

        ResponseEntity<?> response = authController.validarToken("Bearer " + token);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof com.gestion.educativa.identidad.identidad.models.dto.UsuarioValidadoDto);
        com.gestion.educativa.identidad.identidad.models.dto.UsuarioValidadoDto body =
                (com.gestion.educativa.identidad.identidad.models.dto.UsuarioValidadoDto) response.getBody();
        assertEquals("12345678", body.getRunUsuario());
        assertEquals(List.of("ADMIN"), body.getRoles());
    }

    @Test
    void validarTokenRetorna401SinHeaderAuthorization() {
        ResponseEntity<?> response = authController.validarToken(null);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("Token requerido", response.getBody());
    }

    @Test
    void validarTokenRetorna401ConTokenInvalido() {
        when(jwtConfig.validarToken("token-invalido")).thenReturn(false);

        ResponseEntity<?> response = authController.validarToken("Bearer token-invalido");

        assertEquals(401, response.getStatusCode().value());
        assertEquals("Token invalido", response.getBody());
    }
}
