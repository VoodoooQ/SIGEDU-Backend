package com.gestion.educativa.estructura.academica.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.gestion.educativa.estructura.academica.models.dto.CursoDto;
import com.gestion.educativa.estructura.academica.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.estructura.academica.models.request.CursoRequest;
import com.gestion.educativa.estructura.academica.services.CursoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/academica/cursos")
@Tag(name = "Cursos")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar cursos")
    @GetMapping
    public ResponseEntity<List<CursoDto>> list(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener curso por id")
    @GetMapping("/{id}")
    public ResponseEntity<CursoDto> get(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear curso")
    @PostMapping
    public ResponseEntity<CursoDto> create(@Valid @RequestBody CursoRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        CursoDto created = service.create(requestBody);
        return ResponseEntity.created(URI.create("/api/academica/cursos/" + created.getId())).body(created);
    }

    @Operation(summary = "Actualizar curso")
    @PutMapping("/{id}")
    public ResponseEntity<CursoDto> update(@PathVariable Long id, @Valid @RequestBody CursoRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(service.update(id, requestBody));
    }

    @Operation(summary = "Eliminar curso")
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
