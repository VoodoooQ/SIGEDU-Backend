package com.gestion.educativa.convivencia.convivencia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.gestion.educativa.convivencia.convivencia.models.dto.HojaVidaEstudianteDto;
import com.gestion.educativa.convivencia.convivencia.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.convivencia.convivencia.models.request.HojaVidaRequest;
import com.gestion.educativa.convivencia.convivencia.services.HojaVidaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/convivencia/hoja-vida")
@Tag(name = "Hoja de Vida")
public class HojaVidaController {

    private final HojaVidaService service;

    public HojaVidaController(HojaVidaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar hojas de vida")
    @GetMapping
    public ResponseEntity<List<HojaVidaEstudianteDto>> list(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener hoja de vida por id")
    @GetMapping("/{id}")
    public ResponseEntity<HojaVidaEstudianteDto> get(@PathVariable Long id, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "APODERADO", "ESTUDIANTE");
        HojaVidaEstudianteDto dto = service.findById(id);
        if ((tieneRol(usuario, "APODERADO") || tieneRol(usuario, "ESTUDIANTE"))
                && (usuario.getRunUsuario() == null || !usuario.getRunUsuario().equals(dto.getRunEstudianteRef()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Listar hojas de vida por estudiante")
    @GetMapping("/estudiante/{runEstudiante}")
    public ResponseEntity<List<HojaVidaEstudianteDto>> getByEstudiante(@PathVariable String runEstudiante, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "APODERADO", "ESTUDIANTE");
        if ((tieneRol(usuario, "APODERADO") || tieneRol(usuario, "ESTUDIANTE"))
                && (usuario.getRunUsuario() == null || !usuario.getRunUsuario().equals(runEstudiante))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
        return ResponseEntity.ok(service.findByRunEstudianteRef(runEstudiante));
    }

    @Operation(summary = "Crear hoja de vida")
    @PostMapping
    public ResponseEntity<HojaVidaEstudianteDto> create(@Valid @RequestBody HojaVidaRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        String runAutorRef = obtenerUsuario(request).getRunUsuario();
        HojaVidaEstudianteDto created = service.create(requestBody, runAutorRef);
        return ResponseEntity.created(URI.create("/api/convivencia/hoja-vida/" + created.getId())).body(created);
    }

    @Operation(summary = "Actualizar hoja de vida")
    @PutMapping("/{id}")
    public ResponseEntity<HojaVidaEstudianteDto> update(@PathVariable Long id, @Valid @RequestBody HojaVidaRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(service.update(id, requestBody));
    }

    @Operation(summary = "Eliminar hoja de vida")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private UsuarioValidadoDto obtenerUsuario(HttpServletRequest request) {
        return (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
    }

    private void validarPermiso(HttpServletRequest request, String... rolesPermitidos) {
        if (!tieneRol(obtenerUsuario(request), rolesPermitidos)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
    }

    private boolean tieneRol(UsuarioValidadoDto usuario, String... rolesPermitidos) {
        return usuario != null && usuario.getRoles() != null && usuario.getRoles().stream().anyMatch(Arrays.asList(rolesPermitidos)::contains);
    }
}
