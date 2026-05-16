package com.gestion.educativa.geografia.geografia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.educativa.geografia.geografia.models.Comuna;
import com.gestion.educativa.geografia.geografia.services.ComunaService;
@RequestMapping("/api/comunas")
@RestController
public class ComunaController {
    @Autowired
    private ComunaService comunaService;
    @GetMapping("{id_ciudad}")
    public ResponseEntity<List<Comuna>> obtenerComunasPorCiudad(@PathVariable int id_ciudad) {
        List<Comuna> comunas = comunaService.obtenerComunasPorCiudad(id_ciudad);
        return ResponseEntity.ok(comunas);
    }
    
}