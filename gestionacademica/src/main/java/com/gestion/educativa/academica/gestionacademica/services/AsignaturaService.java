package com.gestion.educativa.academica.gestionacademica.services;

import java.util.List;

import com.gestion.educativa.academica.gestionacademica.models.dto.NivelDTO;
import com.gestion.educativa.academica.gestionacademica.models.entity.Asignatura;
import com.gestion.educativa.academica.gestionacademica.models.request.Agregar_Modificar_Asignatura;
import com.gestion.educativa.academica.gestionacademica.repositories.AsignaturaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AsignaturaService {
    @Autowired
    private AsignaturaRepository asignaturaRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ValidarDocenteService validarDocenteService;
    @Value("${microservices.academica.url}")
    private String academicaUrl;

    public Asignatura agregarAsignatura(Agregar_Modificar_Asignatura request) {
        Asignatura nuevaAsignatura = new Asignatura();
        nuevaAsignatura.setNombre_asignatura(request.getNombre_asignatura());
        validarNivel(request.getId_nivel_ref());
        nuevaAsignatura.setId_nivel_ref(request.getId_nivel_ref());

        if (!validarDocenteService.validarDocente(request.getRun_docente_ref())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Docente no valido");
        }
        nuevaAsignatura.setRun_docente_ref(request.getRun_docente_ref());
        return asignaturaRepository.save(nuevaAsignatura);
    }

    public List<Asignatura> obtenerAsignaturas() {
        return asignaturaRepository.findAll();
    }

    public Asignatura obtenerAsignaturaPorId(int id_asignatura) {
        return asignaturaRepository.findById(id_asignatura)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada"));
    }

    public Asignatura modificarAsignatura(int id_asignatura, Agregar_Modificar_Asignatura request) {
        Asignatura asignaturaExistente = obtenerAsignaturaPorId(id_asignatura);
        asignaturaExistente.setNombre_asignatura(request.getNombre_asignatura());
        validarNivel(request.getId_nivel_ref());
        asignaturaExistente.setId_nivel_ref(request.getId_nivel_ref());

        if (!validarDocenteService.validarDocente(request.getRun_docente_ref())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Docente no valido");
        }
        asignaturaExistente.setRun_docente_ref(request.getRun_docente_ref());
        return asignaturaRepository.save(asignaturaExistente);
    }

    public String eliminarAsignatura(int id_asignatura) {
        if (!asignaturaRepository.existsById(id_asignatura)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada");
        }
        asignaturaRepository.deleteById(id_asignatura);
        return "Asignatura eliminada correctamente";
    }

    private void validarNivel(int id_nivel_ref) {
        try {
            ResponseEntity<NivelDTO> response = restTemplate.exchange(
                    academicaUrl + "/api/academica/niveles/{id}",
                    HttpMethod.GET,
                    crearEntidadConAuth(),
                    NivelDTO.class,
                    id_nivel_ref
            );
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nivel no encontrado");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nivel no encontrado");
        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.Forbidden e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No fue posible autorizar la consulta contra Estructura Academica");
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al conectar con el microservicio de Estructura Academica");
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
