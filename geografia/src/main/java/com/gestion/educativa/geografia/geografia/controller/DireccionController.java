package com.gestion.educativa.geografia.geografia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.educativa.geografia.geografia.models.Direccion;
import com.gestion.educativa.geografia.geografia.models.request.AgregarDireccion;
import com.gestion.educativa.geografia.geografia.models.request.ModificarDireccion;
import com.gestion.educativa.geografia.geografia.services.DireccionService;
    
@RequestMapping("/api/direcciones")
@RestController
public class DireccionController {
    @Autowired
    private DireccionService direccionService;
    @PostMapping
    public ResponseEntity<String> crearDireccion(@RequestBody AgregarDireccion direccionRequest) {
        direccionService.agregarDireccion(direccionRequest);
        return ResponseEntity.ok("Dirección creada exitosamente");
    }
    @PutMapping("/modificar/{id}")
    public ResponseEntity<String> modificarDireccion(@PathVariable int id, @RequestBody ModificarDireccion direccionRequest) {
        direccionService.modificarDireccion(id, direccionRequest);
        return ResponseEntity.ok("Dirección modificada exitosamente");
    }
    @GetMapping("{rut_usuario}")
    public ResponseEntity<List<Direccion>> obtenerDireccionPorRun(@PathVariable String rut_usuario) {
        List<Direccion> direcciones = direccionService.obtenerDireccionPorRun(rut_usuario);
        return ResponseEntity.ok(direcciones);
    }
    
}