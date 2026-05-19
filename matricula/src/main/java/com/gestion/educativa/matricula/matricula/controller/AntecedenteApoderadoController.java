package com.gestion.educativa.matricula.matricula.controller;

import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Apoderado;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteApoderado;
import com.gestion.educativa.matricula.matricula.services.AntecedentesApoderadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/antecedenteapoderado")
@RestController
public class AntecedenteApoderadoController {
    @Autowired
    private AntecedentesApoderadoService antecedentesApoderadoService;

    @PostMapping
    public ResponseEntity<Antecedentes_Apoderado> agregarAntecedenteApoderado(@Valid @RequestBody AgregarAntecedenteApoderado entity) {
        Antecedentes_Apoderado nuevoAntecedenteApoderado = antecedentesApoderadoService.registrarAntecedenteApoderado(entity);
        return ResponseEntity.status(201).body(nuevoAntecedenteApoderado);
    }
}
