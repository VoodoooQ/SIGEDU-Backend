package com.gestion.educativa.reuniones.reuniones.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import com.gestion.educativa.reuniones.reuniones.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.reuniones.reuniones.models.entity.Acuerdo;
import com.gestion.educativa.reuniones.reuniones.models.request.AcuerdoRequest;
import com.gestion.educativa.reuniones.reuniones.services.AcuerdoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/acuerdos")
@Tag(name = "Acuerdos")
public class AcuerdoController {

    private final AcuerdoService acuerdoService;

    public AcuerdoController(AcuerdoService acuerdoService) {
        this.acuerdoService = acuerdoService;
    }

    @Operation(summary = "Listar acuerdos")
    @GetMapping
    public ResponseEntity<List<Acuerdo>> listar(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(acuerdoService.listar());
    }

    @Operation(summary = "Crear acuerdo")
    @PostMapping
    public ResponseEntity<Acuerdo> crear(@Valid @RequestBody AcuerdoRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        return ResponseEntity.status(HttpStatus.CREATED).body(acuerdoService.crear(requestBody));
    }

    @Operation(summary = "Actualizar acuerdo")
    @PutMapping("/{id}")
    public ResponseEntity<Acuerdo> actualizar(@PathVariable Long id, @Valid @RequestBody AcuerdoRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        return ResponseEntity.ok(acuerdoService.actualizar(id, requestBody));
    }

    @Operation(summary = "Eliminar acuerdo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        acuerdoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private void validarPermiso(HttpServletRequest request, String... rolesPermitidos) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        if (usuario == null || usuario.getRoles() == null || usuario.getRoles().stream().noneMatch(Arrays.asList(rolesPermitidos)::contains)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
    }
}
