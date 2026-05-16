package com.gestion.educativa.geografia.geografia.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.educativa.geografia.geografia.models.Pais;
import com.gestion.educativa.geografia.geografia.services.PaisService;
@RequestMapping("/api/paises")
@RestController
public class PaisController {
    @Autowired
    private PaisService paisService;
    @GetMapping
    public ResponseEntity<List<Pais>> obtenerPaises() {
        List<Pais> paises = paisService.obtenerPaises();
        return ResponseEntity.ok(paises);
    }
    
}