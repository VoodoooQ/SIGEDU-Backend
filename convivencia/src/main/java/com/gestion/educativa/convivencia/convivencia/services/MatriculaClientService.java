package com.gestion.educativa.convivencia.convivencia.services;

import com.gestion.educativa.convivencia.convivencia.models.dto.MatriculaRefDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatriculaClientService {

    @Value("${matricula.url}")
    private String matriculaUrl;

    private final RestTemplate restTemplate;

    public boolean estudianteMatriculado(String runEstudiante) {
        try {
            ResponseEntity<MatriculaRefDto[]> response = restTemplate.exchange(
                    matriculaUrl + "/api/matricula/{runEstudiante}",
                    HttpMethod.GET,
                    crearEntidadConAuth(),
                    MatriculaRefDto[].class,
                    runEstudiante
            );
            MatriculaRefDto[] body = response.getBody();
            return body != null && body.length > 0;
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        } catch (ResourceAccessException ex) {
            log.warn("Matricula no disponible al consultar estudiante {}", runEstudiante);
            return true;
        } catch (Exception ex) {
            log.warn("No fue posible consultar matricula para estudiante {}", runEstudiante);
            return true;
        }
    }

    private HttpEntity<Void> crearEntidadConAuth() {
        HttpHeaders headers = new HttpHeaders();
        String authHeader = obtenerAuthorizationHeader();
        if (authHeader != null && !authHeader.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, authHeader);
        }
        return new HttpEntity<>(headers);
    }

    private String obtenerAuthorizationHeader() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletAttributes) {
            HttpServletRequest request = servletAttributes.getRequest();
            return request.getHeader(HttpHeaders.AUTHORIZATION);
        }
        return null;
    }
}