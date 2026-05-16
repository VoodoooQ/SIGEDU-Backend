package com.gestion.educativa.matricula.matricula.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.gestion.educativa.matricula.matricula.models.dto.CursoDTO;
import com.gestion.educativa.matricula.matricula.models.dto.PeriodoDTO;
import com.gestion.educativa.matricula.matricula.models.entity.Matricula;
import com.gestion.educativa.matricula.matricula.models.request.AgregarMatricula;
import com.gestion.educativa.matricula.matricula.repositories.MatriculaRepository;

@Service
public class MatriculaService {
    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private ValidarUsuarioService validarUsuarioService;
    @Autowired
    private WebClient academicaWebClient;
    public Matricula registrarMatricula(AgregarMatricula request){
        Matricula nuevaMatricula = new Matricula();
        nuevaMatricula.setAnio_academico(request.getAnio_academico());
        nuevaMatricula.setEstado(request.getEstado());
        
        if (!validarUsuarioService.validarUsuario(request.getRun_estudiante_ref())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estudiante con run " + request.getRun_estudiante_ref() + " no encontrado");
        }
        nuevaMatricula.setRun_estudiante_ref(request.getRun_estudiante_ref());


        CursoDTO curso=null;
        try{
            curso = academicaWebClient.get()
                .uri("/api/academica/cursos/"+request.getId_curso_ref())
                .retrieve()
                .bodyToMono(CursoDTO.class)
                .block();
        }  catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso no encontrado");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al conectar con el microservicio de Estructura Académica");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado de comunicación");
        }
        nuevaMatricula.setId_curso_ref(request.getId_curso_ref());


        PeriodoDTO periodo=null;
        try{
            periodo = academicaWebClient.get()
                .uri("/api/academica/periodos/"+request.getId_periodo_ref())
                .retrieve()
                .bodyToMono(PeriodoDTO.class)
                .block();
        }  catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso no encontrado");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al conectar con el microservicio de Estructura Académica");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado de comunicación");
        }
        nuevaMatricula.setId_periodo_ref(request.getId_periodo_ref());

        return matriculaRepository.save(nuevaMatricula);
    }

    
    public List<Matricula> obtenerMatriculaPorRun(String run_estudiante){
        List<Matricula> matricula = matriculaRepository.findByEstudiante(run_estudiante);
        if (matricula.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula para estudiante con run " + run_estudiante + " no encontrada");
        }
        return matriculaRepository.findByEstudiante(run_estudiante);
    }

}