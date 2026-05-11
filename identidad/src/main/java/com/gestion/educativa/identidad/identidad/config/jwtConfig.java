package com.gestion.educativa.identidad.identidad.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

    @Value("${jwt.secreto}")
    private String secreto;

    @Value("${jwt.expiracion}")
    private long expiracion;

    public String generarToken(String runUsuario, List<String> roles) {
        Date ahora = new Date();
        Date expiracionToken = new Date(ahora.getTime() + expiracion);

        return Jwts.builder()
                .subject(runUsuario)
                .claim("roles", roles)
                .issuedAt(ahora)
                .expiration(expiracionToken)
                .signWith(obtenerClaveFirma())
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(obtenerClaveFirma())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String obtenerRunDesdeToken(String token) {
        return obtenerClaims(token).getSubject();
    }

    public List<String> obtenerRolesDesdeToken(String token) {
        Object roles = obtenerClaims(token).get("roles");
        if (roles instanceof List<?> listaRoles) {
            return listaRoles.stream().map(String::valueOf).toList();
        }
        return List.of();
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey obtenerClaveFirma() {
        return Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }
}
