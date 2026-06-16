package com.gestion.educativa.academica.gestionacademica.services;

import com.gestion.educativa.academica.gestionacademica.models.dto.UsuarioValidadoDto;
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
}
