package com.gestion.educativa.mensajeria.mensajeria.services;

import com.gestion.educativa.mensajeria.mensajeria.models.dto.UsuarioValidadoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class IdentidadClientService {

    @Value("${identidad.url}")
    private String identidadUrl;

    private final RestTemplate restTemplate;

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
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new IllegalArgumentException("Token invalido");
        } catch (ResourceAccessException ex) {
            throw new RuntimeException("Identidad no disponible");
        }
    }
}
