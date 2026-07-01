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
import com.gestion.educativa.geografia.geografia.models.entity.Region;
import com.gestion.educativa.geografia.geografia.services.RegionService;

@RequestMapping("/api/regiones")
@RestController
@Tag(name = "Regiones")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @Operation(summary = "Listar regiones por pa?s")
    @GetMapping("{id_pais}")
    public ResponseEntity<List<Region>> obtenerRegionPorPais(@PathVariable int id_pais, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        List<Region> regiones = regionService.obtenerRegionporPais(id_pais);
        return ResponseEntity.ok(regiones);
    }

    private void validarPermiso(HttpServletRequest request, String... rolesPermitidos) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        if (usuario == null || usuario.getRoles() == null || usuario.getRoles().stream().noneMatch(Arrays.asList(rolesPermitidos)::contains)) {
            throw new IllegalArgumentException("No tiene permisos para esta acci\u00f3n");
        }
    }
}
