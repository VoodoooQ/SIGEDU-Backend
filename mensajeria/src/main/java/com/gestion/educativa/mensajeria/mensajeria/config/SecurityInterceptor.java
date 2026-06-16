package com.gestion.educativa.mensajeria.mensajeria.config;

import com.gestion.educativa.mensajeria.mensajeria.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.mensajeria.mensajeria.services.IdentidadClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SecurityInterceptor implements HandlerInterceptor {

    private final IdentidadClientService identidadClientService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token requerido");
            return false;
        }

        String token = authHeader.substring(7);

        try {
            UsuarioValidadoDto usuario = identidadClientService.validarToken(token);
            request.setAttribute("usuarioAutenticado", usuario);
            return true;
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalido");
            return false;
        } catch (RuntimeException ex) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Identidad no disponible");
            return false;
        }
    }
}
