package com.gestion.educativa.matricula.matricula.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import com.gestion.educativa.matricula.matricula.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Apoderado;
import com.gestion.educativa.matricula.matricula.models.request.AgregarAntecedenteApoderado;
import com.gestion.educativa.matricula.matricula.services.AntecedentesApoderadoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/api/antecedenteapoderado")
@RestController
@Tag(name = "Antecedentes Apoderado")
public class AntecedenteApoderadoController {
    @Autowired
    private AntecedentesApoderadoService antecedentesApoderadoService;

    @Operation(summary = "Registrar antecedente de apoderado")
    @PostMapping
    public ResponseEntity<Antecedentes_Apoderado> agregarAntecedenteApoderado(@Valid @RequestBody AgregarAntecedenteApoderado entity, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.status(201).body(antecedentesApoderadoService.registrarAntecedenteApoderado(entity));
    }

    @Operation(summary = "Obtener antecedente de apoderado")
    @GetMapping("{rut_apoderado}")
    public ResponseEntity<List<Antecedentes_Apoderado>> obtenerAntecedenteApoderadoPorRun(@PathVariable String rut_apoderado, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "APODERADO");
        if (tieneRol(usuario, "APODERADO") && (usuario.getRunUsuario() == null || !usuario.getRunUsuario().equals(rut_apoderado))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
        return ResponseEntity.ok(antecedentesApoderadoService.obtenerAntecedenteApoderadoPorRun(rut_apoderado));
    }

    @Operation(summary = "Actualizar antecedente de apoderado")
    @PutMapping("/{id}")
    public ResponseEntity<Antecedentes_Apoderado> actualizar(@PathVariable int id, @Valid @RequestBody AgregarAntecedenteApoderado entity, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(antecedentesApoderadoService.actualizar(id, entity));
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
