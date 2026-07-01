package com.gestion.educativa.matricula.matricula.services;

import java.util.List;

import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Academicos;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteAcademico;
import com.gestion.educativa.matricula.matricula.repositories.AntecedentesAcademicosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AntecedentesAcademicosService {
    @Autowired
    private AntecedentesAcademicosRepository antecedentesAcademicosRepository;
    @Autowired
    private ValidarUsuarioService validarUsuarioService;

    public Antecedentes_Academicos registrarAntecedenteAcademico(AgregarAntecedenteAcademico request) {
        Antecedentes_Academicos antecedente = new Antecedentes_Academicos();
        aplicarDatos(antecedente, request);
        return antecedentesAcademicosRepository.save(antecedente);
    }

    public List<Antecedentes_Academicos> obtenerAntecedenteAcademicoPorRun(String run_estudiante) {
        List<Antecedentes_Academicos> antecedentes = antecedentesAcademicosRepository.findByEstudiante(run_estudiante);
        if (antecedentes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Antecedente academico para estudiante con run " + run_estudiante + " no encontrado");
        }
        return antecedentes;
    }

    public Antecedentes_Academicos actualizar(int id, AgregarAntecedenteAcademico request) {
        Antecedentes_Academicos antecedente = antecedentesAcademicosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Antecedente academico no encontrado"));
        aplicarDatos(antecedente, request);
        return antecedentesAcademicosRepository.save(antecedente);
    }

    private void aplicarDatos(Antecedentes_Academicos antecedente, AgregarAntecedenteAcademico request) {
        if (!validarUsuarioService.validarUsuario(request.getRun_estudiante_ref())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El run del estudiante no es valido");
        }
        antecedente.setRun_estudiante_ref(request.getRun_estudiante_ref());
        antecedente.setColegio_procedencia(request.getColegio_procedencia());
        antecedente.setPromedio_general(request.getPromedio_general());
    }
}