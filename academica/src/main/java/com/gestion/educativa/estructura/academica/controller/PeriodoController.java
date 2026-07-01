package com.gestion.educativa.estructura.academica.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.gestion.educativa.estructura.academica.models.dto.PeriodoDto;
import com.gestion.educativa.estructura.academica.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.estructura.academica.models.request.PeriodoRequest;
import com.gestion.educativa.estructura.academica.services.PeriodoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/academica/periodos")
@Tag(name = "Periodos")
public class PeriodoController {

    private final PeriodoService service;

    public PeriodoController(PeriodoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar periodos")
    @GetMapping
    public ResponseEntity<List<PeriodoDto>> list(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener periodo por id")
    @GetMapping("/{id}")
    public ResponseEntity<PeriodoDto> get(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear periodo")
    @PostMapping
    public ResponseEntity<PeriodoDto> create(@Valid @RequestBody PeriodoRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        PeriodoDto created = service.create(requestBody);
        return ResponseEntity.created(URI.create("/api/academica/periodos/" + created.getId())).body(created);
    }

    @Operation(summary = "Actualizar periodo")
    @PutMapping("/{id}")
    public ResponseEntity<PeriodoDto> update(@PathVariable Long id, @Valid @RequestBody PeriodoRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(service.update(id, requestBody));
    }

    @Operation(summary = "Eliminar periodo")
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
