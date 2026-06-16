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
import com.gestion.educativa.geografia.geografia.models.entity.Comuna;
import com.gestion.educativa.geografia.geografia.services.ComunaService;

@RequestMapping("/api/comunas")
@RestController
@Tag(name = "Comunas")
public class ComunaController {
    @Autowired
    private ComunaService comunaService;

    @Operation(summary = "Listar comunas por ciudad")
    @GetMapping("{id_ciudad}")
    public ResponseEntity<List<Comuna>> obtenerComunasPorCiudad(@PathVariable int id_ciudad, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        List<Comuna> comunas = comunaService.obtenerComunasPorCiudad(id_ciudad);
        return ResponseEntity.ok(comunas);
    }

    private void validarPermiso(HttpServletRequest request, String... rolesPermitidos) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        if (usuario == null || usuario.getRoles() == null || usuario.getRoles().stream().noneMatch(Arrays.asList(rolesPermitidos)::contains)) {
            throw new IllegalArgumentException("No tiene permisos para esta acci\u00f3n");
        }
    }
}
