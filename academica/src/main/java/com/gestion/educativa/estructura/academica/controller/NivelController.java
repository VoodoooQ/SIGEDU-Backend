package com.gestion.educativa.estructura.academica.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.gestion.educativa.estructura.academica.models.dto.NivelDto;
import com.gestion.educativa.estructura.academica.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.estructura.academica.models.request.NivelRequest;
import com.gestion.educativa.estructura.academica.services.NivelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/academica/niveles")
@Tag(name = "Niveles")
public class NivelController {

    private final NivelService service;

    public NivelController(NivelService service) {
        this.service = service;
    }

    @Operation(summary = "Listar niveles")
    @GetMapping
    public ResponseEntity<List<NivelDto>> list(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener nivel por id")
    @GetMapping("/{id}")
    public ResponseEntity<NivelDto> get(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear nivel")
    @PostMapping
    public ResponseEntity<NivelDto> create(@Valid @RequestBody NivelRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        NivelDto created = service.create(requestBody);
        return ResponseEntity.created(URI.create("/api/academica/niveles/" + created.getId())).body(created);
    }

    @Operation(summary = "Actualizar nivel")
    @PutMapping("/{id}")
    public ResponseEntity<NivelDto> update(@PathVariable Long id, @Valid @RequestBody NivelRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(service.update(id, requestBody));
    }

    @Operation(summary = "Eliminar nivel")
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
