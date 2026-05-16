package com.gestion.educativa.matricula.matricula.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.educativa.matricula.matricula.models.Antecedentes_Apoderado;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteApoderado;
import com.gestion.educativa.matricula.matricula.services.AntecedentesApoderadoService;

@RequestMapping("/api/antecedenteapoderado")
@RestController
public class AntecedenteApoderadoController {
    @Autowired
    private AntecedentesApoderadoService antecedentesApoderadoService;

    //Falta agregar mas variables a agregarAntecedenteApoderado
    public ResponseEntity<Antecedentes_Apoderado> agregarAntecedenteApoderado(AgregarAntecedenteApoderado entity) {
        Antecedentes_Apoderado nuevoAntecedenteApoderado = antecedentesApoderadoService.registrarAntecedenteApoderado(entity);
        return ResponseEntity.status(201).body(nuevoAntecedenteApoderado);
    }

}
