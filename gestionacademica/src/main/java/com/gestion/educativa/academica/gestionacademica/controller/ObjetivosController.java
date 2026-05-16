package com.gestion.educativa.academica.gestionacademica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.educativa.academica.gestionacademica.models.entity.ObjetivosAprendizaje;
import com.gestion.educativa.academica.gestionacademica.models.request.AgregarObjetivo;
import com.gestion.educativa.academica.gestionacademica.services.ObjetivoAprendizajeService;
@RequestMapping("/api/objetivos")
@RestController
public class ObjetivosController {
    @Autowired
    private ObjetivoAprendizajeService objetivoAprendizajeService;
    @PostMapping
    public ResponseEntity<ObjetivosAprendizaje> agregarObjetivoAprendizaje(@RequestBody AgregarObjetivo entity) {
        ObjetivosAprendizaje nuevoObjetivo = objetivoAprendizajeService.agregarObjetivosAprendizaje(entity);
        return ResponseEntity.status(201).body(nuevoObjetivo);
    }
    
    
}