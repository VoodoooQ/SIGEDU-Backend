package com.gestion.educativa.matricula.matricula.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import com.gestion.educativa.matricula.matricula.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Academicos;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteAcademico;
import com.gestion.educativa.matricula.matricula.services.AntecedentesAcademicosService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/api/antecedenteacademico")
@RestController
@Tag(name = "Antecedentes Acad?micos")
public class AntecedenteAcademicoController {
    @Autowired
    private AntecedentesAcademicosService antecedentesAcademicosService;

    @Operation(summary = "Registrar antecedente acad?mico")
    @PostMapping
    public ResponseEntity<Antecedentes_Academicos> agregarAntecedenteAcademico(@Valid @RequestBody AgregarAntecedenteAcademico entity, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.status(201).body(antecedentesAcademicosService.registrarAntecedenteAcademico(entity));
    }

    @Operation(summary = "Obtener antecedente acad?mico por estudiante")
    @GetMapping("{rut_estudiante}")
    public ResponseEntity<List<Antecedentes_Academicos>> obtenerAntecedenteAcademicoPorRun(@PathVariable String rut_estudiante, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "APODERADO");
        return ResponseEntity.ok(antecedentesAcademicosService.obtenerAntecedenteAcademicoPorRun(rut_estudiante));
    }

    @Operation(summary = "Actualizar antecedente acad?mico")
    @PutMapping("/{id}")
    public ResponseEntity<Antecedentes_Academicos> actualizar(@PathVariable int id, @Valid @RequestBody AgregarAntecedenteAcademico entity, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(antecedentesAcademicosService.actualizar(id, entity));
    }

    private UsuarioValidadoDto obtenerUsuario(HttpServletRequest request) {
        return (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
    }

    private void validarPermiso(HttpServletRequest request, String... rolesPermitidos) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        if (usuario == null || usuario.getRoles() == null || usuario.getRoles().stream().noneMatch(Arrays.asList(rolesPermitidos)::contains)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
    }
}
