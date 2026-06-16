package com.gestion.educativa.notas.notas.services;

import com.gestion.educativa.notas.notas.models.dto.AsignaturaRefDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GestionAcademicaClientService {

    @Value("${gestionacademica.url}")
    private String gestionAcademicaUrl;

    private final RestTemplate restTemplate;

    public AsignaturaRefDto obtenerAsignatura(String codigoAsignatura) {
        try {
            ResponseEntity<AsignaturaRefDto> response = restTemplate.getForEntity(
                    gestionAcademicaUrl + "/api/asignatura/{codigoAsignatura}",
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
}
