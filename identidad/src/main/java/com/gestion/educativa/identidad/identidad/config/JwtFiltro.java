package com.gestion.educativa.identidad.identidad.config;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import com.gestion.educativa.identidad.identidad.services.UsuarioDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFiltro extends OncePerRequestFilter {

    private final JwtTokenConfig jwtConfig;
    private final UsuarioDetailsService usuarioDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String rutaSolicitada = request.getServletPath();
        if (rutaSolicitada.startsWith("/swagger-ui") ||
                rutaSolicitada.startsWith("/v3/api-docs") ||
                rutaSolicitada.equals("/api/auth/login") ||
                rutaSolicitada.equals("/api/auth/validar")) {
            filterChain.doFilter(request, response);
            return;
        }

        String encabezadoAutorizacion = request.getHeader("Authorization");
        if (encabezadoAutorizacion == null || !encabezadoAutorizacion.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = encabezadoAutorizacion.substring(7);
        if (jwtConfig.validarToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            String runUsuario = jwtConfig.obtenerRunDesdeToken(token);
            List<String> rolesToken = jwtConfig.obtenerRolesDesdeToken(token);
            Set<String> authorities = rolesToken.stream()
                    .filter(rol -> rol != null && !rol.isBlank())
                    .map(String::trim)
                    .filter(rol -> !rol.isBlank())
                    .map(rol -> rol.toUpperCase(Locale.ROOT))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            if (authorities.contains("ADMIN")) {
                authorities.add("DIRECTIVO");
            }

            UserDetails detallesUsuario = usuarioDetailsService.loadUserByUsername(runUsuario);

            UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(
                    detallesUsuario,
                    null,
                    authorities.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList())
            );
            autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(autenticacion);
        }

        filterChain.doFilter(request, response);
    }
}
