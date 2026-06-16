package com.gestion.educativa.estructura.academica.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.gestion.educativa.estructura.academica.models.dto.SalaDto;
import com.gestion.educativa.estructura.academica.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.estructura.academica.models.request.SalaRequest;
import com.gestion.educativa.estructura.academica.services.SalaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/academica/salas")
@Tag(name = "Salas")
public class SalaController {

    private final SalaService service;

    public SalaController(SalaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar salas")
    @GetMapping
    public ResponseEntity<List<SalaDto>> list(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener sala por id")
    @GetMapping("/{id}")
    public ResponseEntity<SalaDto> get(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear sala")
    @PostMapping
    public ResponseEntity<SalaDto> create(@Valid @RequestBody SalaRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        SalaDto created = service.create(requestBody);
        return ResponseEntity.created(URI.create("/api/academica/salas/" + created.getId())).body(created);
    }

    @Operation(summary = "Actualizar sala")
    @PutMapping("/{id}")
    public ResponseEntity<SalaDto> update(@PathVariable Long id, @Valid @RequestBody SalaRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(service.update(id, requestBody));
    }

    @Operation(summary = "Eliminar sala")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void validarPermiso(HttpServletRequest request, String... rolesPermitidos) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        if (usuario == null || usuario.getRoles() == null || usuario.getRoles().stream().noneMatch(Arrays.asList(rolesPermitidos)::contains)) {
            throw new IllegalArgumentException("No tiene permisos para esta acci\u00f3n");
        }
    }
}
