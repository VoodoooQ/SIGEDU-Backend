package com.gestion.educativa.geografia.geografia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.educativa.geografia.geografia.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.geografia.geografia.models.entity.Ciudad;
import com.gestion.educativa.geografia.geografia.services.CiudadService;

@RequestMapping("/api/ciudades")
@RestController
@Tag(name = "Ciudades")
public class CiudadController {
    @Autowired
    private CiudadService ciudadService;

    @Operation(summary = "Listar ciudades por regi?n")
    @GetMapping("{id_region}")
    public ResponseEntity<List<Ciudad>> obtenerCiudadesPorRegion(@PathVariable int id_region, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        List<Ciudad> ciudades = ciudadService.obtenerCiudadesPorRegion(id_region);
        return ResponseEntity.ok(ciudades);
    }

    private void validarPermiso(HttpServletRequest request, String... rolesPermitidos) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        if (usuario == null || usuario.getRoles() == null || usuario.getRoles().stream().noneMatch(Arrays.asList(rolesPermitidos)::contains)) {
            throw new IllegalArgumentException("No tiene permisos para esta acci\u00f3n");
        }
    }
}
