package com.gestion.educativa.matricula.matricula.config;

import com.gestion.educativa.matricula.matricula.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.matricula.matricula.services.IdentidadClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    private final IdentidadClientService identidadClientService;

    public SecurityInterceptor(IdentidadClientService identidadClientService) {
        this.identidadClientService = identidadClientService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token no proporcionado o invalido");
        }
        String token = authHeader.substring(7);
        UsuarioValidadoDto usuarioValidado = identidadClientService.validarToken(token);
        request.setAttribute("usuarioAutenticado", usuarioValidado);
        return true;
    }
}