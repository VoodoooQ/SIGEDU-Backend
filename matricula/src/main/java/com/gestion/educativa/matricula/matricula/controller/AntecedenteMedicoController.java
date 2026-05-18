package com.gestion.educativa.matricula.matricula.controller;

import java.util.List;
import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Medicos;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteMedico;
import com.gestion.educativa.matricula.matricula.services.AntecedentesMedicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/antecedentemedico")
@RestController
public class AntecedenteMedicoController {
    @Autowired
    private AntecedentesMedicoService antecedentesMedicoService;

    @PostMapping
    public ResponseEntity<Antecedentes_Medicos> agregarAntecedenteMedico(@Valid @RequestBody AgregarAntecedenteMedico entity) {
        Antecedentes_Medicos nuevoAntecedenteMedico = antecedentesMedicoService.agregarAntecedenteMedico(entity);
        return ResponseEntity.status(201).body(nuevoAntecedenteMedico);
    }

    @GetMapping("{rut_estudiante}")
    public ResponseEntity<List<Antecedentes_Medicos>> obtenerAntecedenteMedicoPorRun(@PathVariable String rut_estudiante) {
        List<Antecedentes_Medicos> antecedentesMedicos = antecedentesMedicoService.obtenerAntecedenteMedicoPorRun(rut_estudiante);
        return ResponseEntity.ok(antecedentesMedicos);
    }
}
