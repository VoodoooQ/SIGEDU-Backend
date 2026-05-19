package com.gestion.educativa.matricula.matricula.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Medicos;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteMedico;
import com.gestion.educativa.matricula.matricula.repositories.AntecedenteMedicosRepository;

@Service
public class AntecedentesMedicoService {
    @Autowired
    private AntecedenteMedicosRepository antecedentesMedicosRepository;
    @Autowired
    private ValidarUsuarioService validarUsuarioService;
    public Antecedentes_Medicos agregarAntecedenteMedico(AgregarAntecedenteMedico request) {
        Antecedentes_Medicos nuevoAntecedenteMedico = new Antecedentes_Medicos();

        boolean esAlergico = Boolean.TRUE.equals(request.getAlergico());
        nuevoAntecedenteMedico.setAlergico(esAlergico);
        if (!esAlergico) {
            nuevoAntecedenteMedico.setAlergias(null);   
        }else{
            nuevoAntecedenteMedico.setAlergias(request.getAlergias());
        }
        nuevoAntecedenteMedico.setMedicacion(request.getMedicacion());
        nuevoAntecedenteMedico.setPrevision_salud(request.getPrevision_salud());
        nuevoAntecedenteMedico.setTipo_sangre(request.getTipo_sangre());

        if (!validarUsuarioService.validarUsuario(request.getRun_estudiante_ref())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El run del estudiante no es válido");
        }
        nuevoAntecedenteMedico.setRun_estudiante_ref(request.getRun_estudiante_ref());
        
        return antecedentesMedicosRepository.save(nuevoAntecedenteMedico);
    }
    public List<Antecedentes_Medicos> obtenerAntecedenteMedicoPorRun(String run_estudiante){
        List<Antecedentes_Medicos> antecedentes = antecedentesMedicosRepository.findByEstudiante(run_estudiante);
        if (antecedentes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Antecedente médico para estudiante con run " + run_estudiante + " no encontrado");
        }
        return antecedentesMedicosRepository.findByEstudiante(run_estudiante);
    }

}
