package com.gestion.educativa.geografia.geografia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.educativa.geografia.geografia.models.entity.Ciudad;
import com.gestion.educativa.geografia.geografia.services.CiudadService;

@RequestMapping("/api/ciudades")
@RestController
public class CiudadController {
    @Autowired
    private CiudadService ciudadService;
    @GetMapping("{id_region}")
    public ResponseEntity<List<Ciudad>> obtenerCiudadesPorRegion(@PathVariable int id_region) {
        List<Ciudad> ciudades = ciudadService.obtenerCiudadesPorRegion(id_region);
        return ResponseEntity.ok(ciudades);
    }
}