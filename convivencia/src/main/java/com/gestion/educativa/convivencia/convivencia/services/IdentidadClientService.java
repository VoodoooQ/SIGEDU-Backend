package com.gestion.educativa.convivencia.convivencia.services;

import java.util.Arrays;

import com.gestion.educativa.convivencia.convivencia.models.dto.UsuarioValidadoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class IdentidadClientService {

    @Value("${identidad.url}")
    private String identidadUrl;

    private final RestTemplate restTemplate;

    public IdentidadClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UsuarioValidadoDto validarToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<UsuarioValidadoDto> response = restTemplate.exchange(
                    identidadUrl + "/api/auth/validar",
                    HttpMethod.GET,
                    entity,
                    UsuarioValidadoDto.class
            );
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().value() == 401 || ex.getStatusCode().value() == 403) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token no proporcionado o invalido");
            }
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error al validar token en identidad");
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No fue posible conectar con identidad");
        }
    }

    public boolean estudianteVinculadoAlApoderado(String authorizationHeader, String runEstudiante) {
        if (authorizationHeader == null || authorizationHeader.isBlank() || runEstudiante == null || runEstudiante.isBlank()) {
            return false;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<UsuarioValidadoDto[]> response = restTemplate.exchange(
                    identidadUrl + "/api/usuarios/mis-estudiantes",
                    HttpMethod.GET,
                    entity,
                    UsuarioValidadoDto[].class
            );
            UsuarioValidadoDto[] estudiantes = response.getBody();
            if (estudiantes == null) {
                return false;
            }
            String runNormalizado = normalizarRun(runEstudiante);
            return Arrays.stream(estudiantes)
                    .anyMatch(estudiante -> runNormalizado.equals(normalizarRun(estudiante.getRunUsuario())));
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().value() == 401 || ex.getStatusCode().value() == 403) {
                return false;
            }
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error al consultar estudiantes del apoderado en identidad");
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No fue posible conectar con identidad");
        }
    }

    private String normalizarRun(String run) {
        return run == null ? "" : run.replaceAll("[^0-9]", "").trim();
    }
}
