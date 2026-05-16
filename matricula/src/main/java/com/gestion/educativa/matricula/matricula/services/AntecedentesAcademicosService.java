package com.gestion.educativa.matricula.matricula.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Academicos;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteAcademico;
import com.gestion.educativa.matricula.matricula.repositories.AntecedentesAcademicosRepository;

@Service
public class AntecedentesAcademicosService {
    @Autowired
    private AntecedentesAcademicosRepository antecedentesAcademicosRepository;
    @Autowired
    private ValidarUsuarioService validarUsuarioService;
    public Antecedentes_Academicos registrarAntecedenteAcademico(AgregarAntecedenteAcademico request){
        Antecedentes_Academicos nuevoAntecedenteAcademico = new Antecedentes_Academicos();
        
        if (!validarUsuarioService.validarUsuario(request.getRun_estudiante_ref())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El run del estudiante no es válido");
        }
        nuevoAntecedenteAcademico.setRun_estudiante_ref(request.getRun_estudiante_ref());

        nuevoAntecedenteAcademico.setColegio_procedencia(request.getColegio_procedencia());
        nuevoAntecedenteAcademico.setPromedio_general(request.getPromedio_general());
        return antecedentesAcademicosRepository.save(nuevoAntecedenteAcademico);
    }
    public List<Antecedentes_Academicos> obtenerAntecedenteAcademicoPorRun(String run_estudiante){
        List<Antecedentes_Academicos> antecedentes = antecedentesAcademicosRepository.findByEstudiante(run_estudiante);
        if (antecedentes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Antecedente académico para estudiante con run " + run_estudiante + " no encontrado");
        }
        return antecedentesAcademicosRepository.findByEstudiante(run_estudiante);
    }
}
