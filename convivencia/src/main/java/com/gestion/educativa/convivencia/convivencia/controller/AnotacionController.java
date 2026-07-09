package com.gestion.educativa.convivencia.convivencia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.gestion.educativa.convivencia.convivencia.models.dto.AnotacionesDto;
import com.gestion.educativa.convivencia.convivencia.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.convivencia.convivencia.models.request.AnotacionRequest;
import com.gestion.educativa.convivencia.convivencia.services.AnotacionesService;
import com.gestion.educativa.convivencia.convivencia.services.IdentidadClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/convivencia/anotaciones")
@Tag(name = "Anotaciones")
public class AnotacionController {

    private final AnotacionesService service;
    private final IdentidadClientService identidadClientService;

    public AnotacionController(AnotacionesService service, IdentidadClientService identidadClientService) {
        this.service = service;
        this.identidadClientService = identidadClientService;
    }

    @Operation(summary = "Listar anotaciones")
    @GetMapping
    public ResponseEntity<List<AnotacionesDto>> list(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener anotaci?n por id")
    @GetMapping("/{id}")
    public ResponseEntity<AnotacionesDto> get(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE");
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Listar anotaciones por estudiante")
    @GetMapping("/estudiante/{runEstudiante}")
    public ResponseEntity<List<AnotacionesDto>> getByEstudiante(@PathVariable String runEstudiante, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "APODERADO", "ESTUDIANTE");
        validarAccesoEstudiante(request, usuario, runEstudiante);
        return ResponseEntity.ok(service.findByRunEstudianteRef(runEstudiante));
    }

    @Operation(summary = "Crear anotaci?n")
    @PostMapping
    public ResponseEntity<AnotacionesDto> create(@Valid @RequestBody AnotacionRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE");
        String runAutorRef = obtenerUsuario(request).getRunUsuario();
        AnotacionesDto created = service.create(requestBody, runAutorRef);
        return ResponseEntity.created(URI.create("/api/convivencia/anotaciones/" + created.getId())).body(created);
    }

    @Operation(summary = "Actualizar anotaci?n")
    @PutMapping("/{id}")
    public ResponseEntity<AnotacionesDto> update(@PathVariable Long id, @Valid @RequestBody AnotacionRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(service.update(id, requestBody));
    }

    @Operation(summary = "Eliminar anotaci?n")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    private void validarAccesoEstudiante(HttpServletRequest request, UsuarioValidadoDto usuario, String runEstudiante) {
        if (tieneRol(usuario, "ESTUDIANTE") && !normalizarRun(usuario.getRunUsuario()).equals(normalizarRun(runEstudiante))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
        if (tieneRol(usuario, "APODERADO")
                && !identidadClientService.estudianteVinculadoAlApoderado(request.getHeader(HttpHeaders.AUTHORIZATION), runEstudiante)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
    }

    private String normalizarRun(String run) {
        return run == null ? "" : run.replaceAll("[^0-9]", "").trim();
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
