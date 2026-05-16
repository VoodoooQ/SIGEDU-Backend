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
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token requerido o invalido");
            return false;
        }

        String token = authorizationHeader.substring(7);
        try {
            UsuarioValidadoDto usuarioValidado = identidadClientService.validarToken(token);
            request.setAttribute("usuarioAutenticado", usuarioValidado);
            return true;
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token requerido o invalido");
            return false;
        } catch (RuntimeException ex) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "No se pudo validar el token");
            return false;
        }
    }
}
