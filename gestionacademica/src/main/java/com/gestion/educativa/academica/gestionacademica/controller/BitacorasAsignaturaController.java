package com.gestion.educativa.academica.gestionacademica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.educativa.academica.gestionacademica.models.BitacoraAsignatura;
import com.gestion.educativa.academica.gestionacademica.models.request.AgregarBitacora;
import com.gestion.educativa.academica.gestionacademica.models.request.ModificarBitacora;
import com.gestion.educativa.academica.gestionacademica.services.BitacoraAsignaturaService;
@RequestMapping("/api/bitacora")
@RestController
public class BitacorasAsignaturaController {
    @Autowired
    private BitacoraAsignaturaService bitacoraAsignaturaService;
    @PostMapping
    public ResponseEntity<BitacoraAsignatura> registrarBitacoraAsignatura(@RequestBody AgregarBitacora entity) {
        BitacoraAsignatura nuevaBitacora = bitacoraAsignaturaService.registrarBitacoraAsignatura(entity);
        return ResponseEntity.status(201).body(nuevaBitacora);
    }
    @PutMapping("{id}")
    public ResponseEntity<BitacoraAsignatura> modificarBitacoraAsignatura(@PathVariable int id, @RequestBody ModificarBitacora entity) {
        BitacoraAsignatura bitacoraActualizada = bitacoraAsignaturaService.modificarBitacoraAsignatura(id, entity);
        return ResponseEntity.ok(bitacoraActualizada);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> eliminarBitacoraAsignatura(@PathVariable int id) {
        String mensaje = bitacoraAsignaturaService.eliminarBitacoraAsignatura(id);
        return ResponseEntity.ok(mensaje);
    }

}