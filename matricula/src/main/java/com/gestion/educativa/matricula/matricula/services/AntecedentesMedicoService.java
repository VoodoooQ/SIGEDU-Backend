package com.gestion.educativa.matricula.matricula.services;

import java.util.List;

import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Medicos;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteMedico;
import com.gestion.educativa.matricula.matricula.repositories.AntecedenteMedicosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AntecedentesMedicoService {
    @Autowired
    private AntecedenteMedicosRepository antecedentesMedicosRepository;
    @Autowired
    private ValidarUsuarioService validarUsuarioService;

    public Antecedentes_Medicos agregarAntecedenteMedico(AgregarAntecedenteMedico request) {
        Antecedentes_Medicos antecedente = new Antecedentes_Medicos();
        aplicarDatos(antecedente, request);
        return antecedentesMedicosRepository.save(antecedente);
    }

    public List<Antecedentes_Medicos> obtenerAntecedenteMedicoPorRun(String run_estudiante) {
        List<Antecedentes_Medicos> antecedentes = antecedentesMedicosRepository.findByEstudiante(run_estudiante);
        if (antecedentes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Antecedente medico para estudiante con run " + run_estudiante + " no encontrado");
        }
        return antecedentes;
    }

    public Antecedentes_Medicos actualizar(int id, AgregarAntecedenteMedico request) {
        Antecedentes_Medicos antecedente = antecedentesMedicosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Antecedente medico no encontrado"));
        aplicarDatos(antecedente, request);
        return antecedentesMedicosRepository.save(antecedente);
    }

    private void aplicarDatos(Antecedentes_Medicos antecedente, AgregarAntecedenteMedico request) {
        boolean esAlergico = Boolean.TRUE.equals(request.getAlergico());
        antecedente.setAlergico(esAlergico);
        antecedente.setAlergias(esAlergico ? request.getAlergias() : null);
        antecedente.setMedicacion(request.getMedicacion());
        antecedente.setPrevision_salud(request.getPrevision_salud());
        antecedente.setTipo_sangre(request.getTipo_sangre());
        if (!validarUsuarioService.validarUsuario(request.getRun_estudiante_ref())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El run del estudiante no es valido");
        }
        antecedente.setRun_estudiante_ref(request.getRun_estudiante_ref());
    }
}