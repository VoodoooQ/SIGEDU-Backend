package com.gestion.educativa.academica.gestionacademica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.educativa.academica.gestionacademica.models.entity.Asignatura;
import com.gestion.educativa.academica.gestionacademica.models.request.Agregar_Modificar_Asignatura;
import com.gestion.educativa.academica.gestionacademica.services.AsignaturaService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RequestMapping("/api/asignatura")
@RestController
public class AsignaturaController {
    @Autowired
    private AsignaturaService asignaturaservice;
    @GetMapping
    public ResponseEntity<List<Asignatura>> obtenerAsignaturas() {
        List<Asignatura> asignaturas = asignaturaservice.obtenerAsignaturas();
        if (asignaturaservice.obtenerAsignaturas().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(asignaturas);
    }
    @PostMapping
    public ResponseEntity<Asignatura> postMethodName(@RequestBody Agregar_Modificar_Asignatura entity) {
        Asignatura nuevaAsignatura = asignaturaservice.agregarAsignatura(entity);
        return ResponseEntity.status(201).body(nuevaAsignatura);
    }
    @PutMapping("/modificar/{id}")
    public ResponseEntity<Asignatura> modificarAsignatura(@PathVariable int id, @RequestBody Agregar_Modificar_Asignatura entity) {
        Asignatura asignaturaActualizada = asignaturaservice.modificarAsignatura(id, entity);
        return ResponseEntity.ok(asignaturaActualizada);

    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> eliminarAsignatura(@PathVariable int id) {
        String mensaje = asignaturaservice.eliminarAsignatura(id);
        return ResponseEntity.ok(mensaje);
    }
}