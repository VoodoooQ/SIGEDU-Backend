package com.gestion.educativa.academica.gestionacademica.controller;

import com.gestion.educativa.academica.gestionacademica.models.entity.ObjetivosAprendizaje;
import com.gestion.educativa.academica.gestionacademica.models.request.AgregarObjetivo;
import com.gestion.educativa.academica.gestionacademica.services.ObjetivoAprendizajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/objetivos")
@RestController
public class ObjetivosController {
    @Autowired
    private ObjetivoAprendizajeService objetivoAprendizajeService;

    @PostMapping
    public ResponseEntity<ObjetivosAprendizaje> agregarObjetivoAprendizaje(@Valid @RequestBody AgregarObjetivo entity) {
        ObjetivosAprendizaje nuevoObjetivo = objetivoAprendizajeService.agregarObjetivosAprendizaje(entity);
        return ResponseEntity.status(201).body(nuevoObjetivo);
    }
}
