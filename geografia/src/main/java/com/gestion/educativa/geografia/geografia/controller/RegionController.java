package com.gestion.educativa.geografia.geografia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gestion.educativa.geografia.geografia.models.entity.Region;
import com.gestion.educativa.geografia.geografia.services.RegionService;

@RequestMapping("/api/regiones")
@RestController
public class RegionController {
    @Autowired
    private RegionService regionService;
    @GetMapping("{id_pais}")
    public ResponseEntity<List<Region>> obtenerRegionPorPais(@PathVariable int id_pais) {
        List<Region> regiones = regionService.obtenerRegionporPais(id_pais);
        return ResponseEntity.ok(regiones);
    }
    
}