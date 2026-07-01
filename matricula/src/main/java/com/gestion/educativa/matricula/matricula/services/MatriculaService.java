package com.gestion.educativa.matricula.matricula.services;

import java.util.List;

import com.gestion.educativa.matricula.matricula.models.dto.CursoDTO;
import com.gestion.educativa.matricula.matricula.models.dto.PeriodoDTO;
import com.gestion.educativa.matricula.matricula.models.entity.Matricula;
import com.gestion.educativa.matricula.matricula.models.request.AgregarMatricula;
import com.gestion.educativa.matricula.matricula.repositories.MatriculaRepository;
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
public class MatriculaService {
    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private ValidarUsuarioService validarUsuarioService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${microservices.academica.url}")
    private String academicaUrl;

    public Matricula registrarMatricula(AgregarMatricula request) {
        Matricula nuevaMatricula = new Matricula();
        aplicarDatos(nuevaMatricula, request);
        return matriculaRepository.save(nuevaMatricula);
    }

    public List<Matricula> listarTodas() {
        return matriculaRepository.findAll();
    }

    public List<Matricula> obtenerMatriculaPorRun(String run_estudiante) {
        List<Matricula> matricula = matriculaRepository.findByEstudiante(run_estudiante);
        if (matricula.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Matricula para estudiante con run " + run_estudiante + " no encontrada");
        }
        return matricula;
    }

    public Matricula actualizar(int id, AgregarMatricula request) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matricula no encontrada"));
        aplicarDatos(matricula, request);
        return matriculaRepository.save(matricula);
    }

    public void eliminar(int id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matricula no encontrada"));
        matriculaRepository.delete(matricula);
    }

    private void aplicarDatos(Matricula matricula, AgregarMatricula request) {
        matricula.setAnio_academico(request.getAnio_academico());
        matricula.setEstado(request.getEstado());
        if (!validarUsuarioService.validarUsuario(request.getRun_estudiante_ref())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estudiante con run " + request.getRun_estudiante_ref() + " no encontrado");
        }
        matricula.setRun_estudiante_ref(request.getRun_estudiante_ref());
        validarCurso(request.getId_curso_ref());
        matricula.setId_curso_ref(request.getId_curso_ref());
        validarPeriodo(request.getId_periodo_ref());
        matricula.setId_periodo_ref(request.getId_periodo_ref());
    }

    private void validarCurso(int idCurso) {
        try {
            ResponseEntity<CursoDTO> response = restTemplate.exchange(
                    academicaUrl + "/api/academica/cursos/{id}",
                    HttpMethod.GET,
                    crearEntidadConAuth(),
                    CursoDTO.class,
                    idCurso
            );
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso no encontrado");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso no encontrado");
        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.Forbidden e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No fue posible autorizar la consulta contra Estructura Academica");
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al conectar con el microservicio de Estructura Academica");
        }
    }

    private void validarPeriodo(int idPeriodo) {
        try {
            ResponseEntity<PeriodoDTO> response = restTemplate.exchange(
                    academicaUrl + "/api/academica/periodos/{id}",
                    HttpMethod.GET,
                    crearEntidadConAuth(),
                    PeriodoDTO.class,
                    idPeriodo
            );
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Periodo no encontrado");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Periodo no encontrado");
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