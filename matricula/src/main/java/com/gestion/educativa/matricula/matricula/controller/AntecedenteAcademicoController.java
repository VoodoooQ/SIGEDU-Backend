package com.gestion.educativa.matricula.matricula.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Academicos;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteAcademico;
import com.gestion.educativa.matricula.matricula.services.AntecedentesAcademicosService;

@RequestMapping("/api/antecedenteacademico")
@RestController
public class AntecedenteAcademicoController {
    @Autowired
    private AntecedentesAcademicosService antecedentesAcademicosService;
    @PostMapping
    public ResponseEntity<Antecedentes_Academicos> agregarAntecedenteAcademico(@RequestBody AgregarAntecedenteAcademico entity) {
        Antecedentes_Academicos nuevoAntecedenteAcademico = antecedentesAcademicosService.registrarAntecedenteAcademico(entity);
        return ResponseEntity.status(201).body(nuevoAntecedenteAcademico);
    }
    @GetMapping("{rut_estudiante}")
    public ResponseEntity<List<Antecedentes_Academicos>> obtenerAntecedenteAcademicoPorRun(@PathVariable String rut_estudiante) {
        List<Antecedentes_Academicos> antecedentesAcademicos = antecedentesAcademicosService.obtenerAntecedenteAcademicoPorRun(rut_estudiante);
        return ResponseEntity.ok(antecedentesAcademicos);
    }

}
