package com.gestion.educativa.matricula.matricula.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gestion.educativa.matricula.matricula.models.Matricula;
import com.gestion.educativa.matricula.matricula.models.request.AgregarMatricula;
import com.gestion.educativa.matricula.matricula.repositories.MatriculaRepository;

@Service
public class MatriculaService {
    @Autowired
    private MatriculaRepository matriculaRepository;

    public Matricula registrarMatricula(AgregarMatricula request){
        Matricula nuevaMatricula = new Matricula();
        nuevaMatricula.setAnio_academico(request.getAnio_academico());
        nuevaMatricula.setEstado(request.getEstado());
        //Validar run estudiante si existe en MS0 antes de registrar el antecedente academico
        nuevaMatricula.setRun_estudiante_ref(request.getRun_estudiante_ref());
        nuevaMatricula.setId_curso_ref(request.getId_curso_ref());
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