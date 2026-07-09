package com.gestion.educativa.matricula.matricula.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import com.gestion.educativa.matricula.matricula.models.dto.AsistenciaResumenDto;
import com.gestion.educativa.matricula.matricula.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.matricula.matricula.models.entity.Asistencia;
import com.gestion.educativa.matricula.matricula.models.request.AsistenciaRequest;
import com.gestion.educativa.matricula.matricula.services.AsistenciaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/matricula/asistencia")
@Tag(name = "Asistencia")
public class AsistenciaController {
    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @Operation(summary = "Listar asistencia por estudiante")
    @GetMapping("/estudiante/{runEstudiante}")
    public ResponseEntity<List<Asistencia>> listarPorEstudiante(@PathVariable String runEstudiante, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "APODERADO", "ESTUDIANTE");
        return ResponseEntity.ok(asistenciaService.listarPorEstudiante(runEstudiante));
    }

    @Operation(summary = "Resumen de asistencia por estudiante")
    @GetMapping("/estudiante/{runEstudiante}/resumen")
    public ResponseEntity<AsistenciaResumenDto> resumenPorEstudiante(@PathVariable String runEstudiante, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "APODERADO", "ESTUDIANTE");
        return ResponseEntity.ok(asistenciaService.resumenPorEstudiante(runEstudiante));
    }

    @Operation(summary = "Registrar asistencia")
    @PostMapping
    public ResponseEntity<Asistencia> registrar(@Valid @RequestBody AsistenciaRequest body, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE");
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(asistenciaService.registrar(body, usuario.getRunUsuario()));
    }

    @Operation(summary = "Eliminar asistencia")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        asistenciaService.eliminar(id);
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
