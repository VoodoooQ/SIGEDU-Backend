package com.gestion.educativa.reuniones.reuniones.controller;

import java.util.List;
import com.gestion.educativa.reuniones.reuniones.models.entity.BitacoraReunionApoderado;
import com.gestion.educativa.reuniones.reuniones.models.entity.BitacoraReunionGeneral;
import com.gestion.educativa.reuniones.reuniones.models.entity.BitacoraReunionP1aP1;
import com.gestion.educativa.reuniones.reuniones.services.ReunionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reuniones")
public class ReunionController {

    private final ReunionService reunionService;

    public ReunionController(ReunionService reunionService) {
        this.reunionService = reunionService;
    }

    @GetMapping("/generales")
    public ResponseEntity<List<BitacoraReunionGeneral>> listarGenerales() {
        return ResponseEntity.ok(reunionService.listarGenerales());
    }

    @PostMapping("/generales")
    public ResponseEntity<BitacoraReunionGeneral> crearGeneral(@RequestBody BitacoraReunionGeneral reunionGeneral) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reunionService.guardarGeneral(reunionGeneral));
    }

    @PutMapping("/generales/{id}")
    public ResponseEntity<BitacoraReunionGeneral> actualizarGeneral(
            @PathVariable Long id,
            @RequestBody BitacoraReunionGeneral reunionGeneral) {
        reunionGeneral.setIdBitacoraReunionGeneral(id);
        return ResponseEntity.ok(reunionService.guardarGeneral(reunionGeneral));
    }

    @DeleteMapping("/generales/{id}")
    public ResponseEntity<Void> eliminarGeneral(@PathVariable Long id) {
        reunionService.eliminarGeneral(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/apoderados")
    public ResponseEntity<List<BitacoraReunionApoderado>> listarApoderados() {
        return ResponseEntity.ok(reunionService.listarApoderados());
    }

    @PostMapping("/apoderados")
    public ResponseEntity<BitacoraReunionApoderado> crearApoderado(@RequestBody BitacoraReunionApoderado reunionApoderado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reunionService.guardarApoderado(reunionApoderado));
    }

    @PutMapping("/apoderados/{id}")
    public ResponseEntity<BitacoraReunionApoderado> actualizarApoderado(
            @PathVariable Long id,
            @RequestBody BitacoraReunionApoderado reunionApoderado) {
        reunionApoderado.setIdBitacoraReunionApoderado(id);
        return ResponseEntity.ok(reunionService.guardarApoderado(reunionApoderado));
    }

    @DeleteMapping("/apoderados/{id}")
    public ResponseEntity<Void> eliminarApoderado(@PathVariable Long id) {
        reunionService.eliminarApoderado(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/p1a1")
    public ResponseEntity<List<BitacoraReunionP1aP1>> listarP1aP1() {
        return ResponseEntity.ok(reunionService.listarP1aP1());
    }

    @PostMapping("/p1a1")
    public ResponseEntity<BitacoraReunionP1aP1> crearP1aP1(@RequestBody BitacoraReunionP1aP1 reunionP1aP1) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reunionService.guardarP1aP1(reunionP1aP1));
    }

    @PutMapping("/p1a1/{id}")
    public ResponseEntity<BitacoraReunionP1aP1> actualizarP1aP1(
            @PathVariable Long id,
            @RequestBody BitacoraReunionP1aP1 reunionP1aP1) {
        reunionP1aP1.setIdBitacoraReunionP1aP1(id);
        return ResponseEntity.ok(reunionService.guardarP1aP1(reunionP1aP1));
    }

    @DeleteMapping("/p1a1/{id}")
    public ResponseEntity<Void> eliminarP1aP1(@PathVariable Long id) {
        reunionService.eliminarP1aP1(id);
        return ResponseEntity.noContent().build();
    }
}