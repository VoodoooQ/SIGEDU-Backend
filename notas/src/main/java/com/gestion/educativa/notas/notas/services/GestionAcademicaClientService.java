package com.gestion.educativa.notas.notas.services;

import com.gestion.educativa.notas.notas.models.dto.AsignaturaRefDto;
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
public class GestionAcademicaClientService {

    @Value("${gestionacademica.url}")
    private String gestionAcademicaUrl;

    private final RestTemplate restTemplate;

    public AsignaturaRefDto obtenerAsignatura(String codigoAsignatura) {
        try {
            ResponseEntity<AsignaturaRefDto> response = restTemplate.exchange(
                    gestionAcademicaUrl + "/api/asignatura/{codigoAsignatura}",
                    HttpMethod.GET,
                    crearEntidadConAuth(),
                    AsignaturaRefDto.class,
                    codigoAsignatura
            );
            return response.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            return null;
        } catch (ResourceAccessException ex) {
            log.warn("GestionAcademica no disponible al consultar asignatura {}", codigoAsignatura);
            return null;
        } catch (Exception ex) {
            log.warn("No fue posible consultar asignatura {} en gestionacademica", codigoAsignatura);
            return null;
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