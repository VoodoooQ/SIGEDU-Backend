package com.gestion.educativa.reuniones.reuniones.controller;

import java.util.List;
import com.gestion.educativa.reuniones.reuniones.models.entity.Acuerdo;
import com.gestion.educativa.reuniones.reuniones.models.request.AcuerdoRequest;
import com.gestion.educativa.reuniones.reuniones.services.AcuerdoService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/acuerdos")
public class AcuerdoController {

    private final AcuerdoService acuerdoService;

    public AcuerdoController(AcuerdoService acuerdoService) {
        this.acuerdoService = acuerdoService;
    }

    @GetMapping
    public ResponseEntity<List<Acuerdo>> listar() {
        return ResponseEntity.ok(acuerdoService.listar());
    }

    @PostMapping
    public ResponseEntity<Acuerdo> crear(@Valid @RequestBody AcuerdoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(acuerdoService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Acuerdo> actualizar(@PathVariable Long id, @Valid @RequestBody AcuerdoRequest request) {
        return ResponseEntity.ok(acuerdoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        acuerdoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
