package com.gestion.educativa.estructura.academica.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import com.gestion.educativa.estructura.academica.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.estructura.academica.models.entity.Configuracion;
import com.gestion.educativa.estructura.academica.models.request.ConfiguracionRequest;
import com.gestion.educativa.estructura.academica.services.ConfiguracionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/academica/configuraciones")
@Tag(name = "Configuraciones")
public class ConfiguracionController {
    private final ConfiguracionService service;

    public ConfiguracionController(ConfiguracionService service) {
        this.service = service;
    }

    @Operation(summary = "Listar configuraciones")
    @GetMapping
    public ResponseEntity<List<Configuracion>> list(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Crear configuracion")
    @PostMapping
    public ResponseEntity<Configuracion> create(@Valid @RequestBody ConfiguracionRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        Configuracion created = service.create(requestBody);
        return ResponseEntity.created(URI.create("/api/academica/configuraciones/" + created.getId())).body(created);
    }

    @Operation(summary = "Actualizar configuracion")
    @PutMapping("/{id}")
    public ResponseEntity<Configuracion> update(@PathVariable Long id, @Valid @RequestBody ConfiguracionRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        return ResponseEntity.ok(service.update(id, requestBody));
    }

    @Operation(summary = "Eliminar configuracion")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void validarPermiso(HttpServletRequest request, String... rolesPermitidos) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        if (usuario == null || usuario.getRoles() == null || usuario.getRoles().stream().noneMatch(Arrays.asList(rolesPermitidos)::contains)) {
            throw new IllegalArgumentException("No tiene permisos para esta accion");
        }
    }
}
