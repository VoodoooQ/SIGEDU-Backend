package com.gestion.educativa.matricula.matricula.services;

import java.util.List;

import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Apoderado;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteApoderado;
import com.gestion.educativa.matricula.matricula.repositories.AntecedentesApoderadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AntecedentesApoderadoService {
    @Autowired
    private AntecedentesApoderadoRepository antecedentesApoderadoRepository;

    public Antecedentes_Apoderado registrarAntecedenteApoderado(AgregarAntecedenteApoderado request) {
        Antecedentes_Apoderado antecedente = new Antecedentes_Apoderado();
        antecedente.setRun_apoderado_ref(request.getRun_apoderado_ref());
        return antecedentesApoderadoRepository.save(antecedente);
    }

    public List<Antecedentes_Apoderado> obtenerAntecedenteApoderadoPorRun(String run_apoderado) {
        List<Antecedentes_Apoderado> antecedentes = antecedentesApoderadoRepository.findByApoderado(run_apoderado);
        if (antecedentes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Antecedente de apoderado con run " + run_apoderado + " no encontrado");
        }
        return antecedentes;
    }

    public Antecedentes_Apoderado actualizar(int id, AgregarAntecedenteApoderado request) {
        Antecedentes_Apoderado antecedente = antecedentesApoderadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Antecedente de apoderado no encontrado"));
        antecedente.setRun_apoderado_ref(request.getRun_apoderado_ref());
        return antecedentesApoderadoRepository.save(antecedente);
    }
}