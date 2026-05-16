package com.gestion.educativa.matricula.matricula.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.educativa.matricula.matricula.models.Antecedentes_Apoderado;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteApoderado;
import com.gestion.educativa.matricula.matricula.repositories.AntecedentesApoderadoRepository;
@Service
public class AntecedentesApoderadoService {
    @Autowired
    private AntecedentesApoderadoRepository antecedentesApoderadoRepository;
    public Antecedentes_Apoderado registrarAntecedenteApoderado(AgregarAntecedenteApoderado request){
        //Ver si existen mas variables para registrar en el antecedente del apoderado, por ahora solo se registra el run del apoderado
        Antecedentes_Apoderado nuevoAntecedenteApoderado = new Antecedentes_Apoderado();
        nuevoAntecedenteApoderado.setRun_apoderado_ref(request.getRun_apoderado_ref());

        return antecedentesApoderadoRepository.save(nuevoAntecedenteApoderado);
    }

}
