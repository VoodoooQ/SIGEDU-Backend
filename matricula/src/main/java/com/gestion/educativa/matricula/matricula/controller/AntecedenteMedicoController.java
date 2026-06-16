package com.gestion.educativa.matricula.matricula.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import com.gestion.educativa.matricula.matricula.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Medicos;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteMedico;
import com.gestion.educativa.matricula.matricula.services.AntecedentesMedicoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/api/antecedentemedico")
@RestController
@Tag(name = "Antecedentes M?dicos")
public class AntecedenteMedicoController {
    @Autowired
    private AntecedentesMedicoService antecedentesMedicoService;

    @Operation(summary = "Registrar antecedente m?dico")
    @PostMapping
    public ResponseEntity<Antecedentes_Medicos> agregarAntecedenteMedico(@Valid @RequestBody AgregarAntecedenteMedico entity, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.status(201).body(antecedentesMedicoService.agregarAntecedenteMedico(entity));
    }

    @Operation(summary = "Obtener antecedente m?dico por estudiante")
    @GetMapping("{rut_estudiante}")
    public ResponseEntity<List<Antecedentes_Medicos>> obtenerAntecedenteMedicoPorRun(@PathVariable String rut_estudiante, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "APODERADO");
        return ResponseEntity.ok(antecedentesMedicoService.obtenerAntecedenteMedicoPorRun(rut_estudiante));
    }

    @Operation(summary = "Actualizar antecedente m?dico")
    @PutMapping("/{id}")
    public ResponseEntity<Antecedentes_Medicos> actualizar(@PathVariable int id, @Valid @RequestBody AgregarAntecedenteMedico entity, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(antecedentesMedicoService.actualizar(id, entity));
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
