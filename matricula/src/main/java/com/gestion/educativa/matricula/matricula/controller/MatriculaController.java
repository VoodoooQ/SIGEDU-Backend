package com.gestion.educativa.matricula.matricula.controller;

import java.util.List;
import com.gestion.educativa.matricula.matricula.models.entity.Matricula;
import com.gestion.educativa.matricula.matricula.models.request.AgregarMatricula;
import com.gestion.educativa.matricula.matricula.services.MatriculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/matricula")
@RestController
public class MatriculaController {
    @Autowired
    private MatriculaService matriculaService;

    @PostMapping
    public ResponseEntity<Matricula> agregarMatricula(@Valid @RequestBody AgregarMatricula entity) {
        Matricula nuevaMatricula = matriculaService.registrarMatricula(entity);
        return ResponseEntity.status(201).body(nuevaMatricula);
    }

    @GetMapping("{rut_estudiante}")
    public ResponseEntity<List<Matricula>> obtenerMatricula(@PathVariable String rut_estudiante) {
        List<Matricula> matriculas = matriculaService.obtenerMatriculaPorRun(rut_estudiante);
        return ResponseEntity.ok(matriculas);
    }
}
