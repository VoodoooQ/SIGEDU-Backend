package com.gestion.educativa.matricula.matricula.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import com.gestion.educativa.matricula.matricula.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.matricula.matricula.models.entity.Matricula;
import com.gestion.educativa.matricula.matricula.models.request.AgregarMatricula;
import com.gestion.educativa.matricula.matricula.services.MatriculaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/api/matricula")
@RestController
@Tag(name = "Matr?culas")
public class MatriculaController {
    @Autowired
    private MatriculaService matriculaService;

    @Operation(summary = "Listar matr?culas")
    @GetMapping
    public ResponseEntity<List<Matricula>> listarTodas(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE");
        return ResponseEntity.ok(matriculaService.listarTodas());
    }

    @Operation(summary = "Registrar matr?cula")
    @PostMapping
    public ResponseEntity<Matricula> agregarMatricula(@Valid @RequestBody AgregarMatricula entity, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.status(201).body(matriculaService.registrarMatricula(entity));
    }

    @Operation(summary = "Obtener matr?culas por estudiante")
    @GetMapping("{rut_estudiante}")
    public ResponseEntity<List<Matricula>> obtenerMatricula(@PathVariable String rut_estudiante, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "APODERADO");
        if (tieneRol(usuario, "APODERADO") && (usuario.getRunUsuario() == null || !usuario.getRunUsuario().equals(rut_estudiante))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
        return ResponseEntity.ok(matriculaService.obtenerMatriculaPorRun(rut_estudiante));
    }

    @Operation(summary = "Actualizar matr?cula")
    @PutMapping("/{id}")
    public ResponseEntity<Matricula> actualizar(@PathVariable int id, @Valid @RequestBody AgregarMatricula entity, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(matriculaService.actualizar(id, entity));
    }

    @Operation(summary = "Eliminar matr?cula")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        matriculaService.eliminar(id);
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
