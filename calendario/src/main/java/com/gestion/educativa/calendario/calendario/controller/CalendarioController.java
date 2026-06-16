package com.gestion.educativa.calendario.calendario.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.gestion.educativa.calendario.calendario.models.dto.EventoCalendarioDto;
import com.gestion.educativa.calendario.calendario.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.calendario.calendario.models.request.EventoCalendarioRequest;
import com.gestion.educativa.calendario.calendario.services.CalendarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/calendario/eventos")
@Tag(name = "Calendario")
public class CalendarioController {

    private final CalendarioService service;

    public CalendarioController(CalendarioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar eventos")
    @GetMapping
    public ResponseEntity<List<EventoCalendarioDto>> list(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener evento por id")
    @GetMapping("/{id}")
    public ResponseEntity<EventoCalendarioDto> get(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Listar eventos por fecha")
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<EventoCalendarioDto>> getByFecha(@PathVariable LocalDate fecha, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(service.findByFecha(fecha));
    }

    @Operation(summary = "Listar eventos por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<EventoCalendarioDto>> getByTipo(@PathVariable String tipo, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(service.findByTipo(tipo));
    }

    @Operation(summary = "Crear evento")
    @PostMapping
    public ResponseEntity<EventoCalendarioDto> create(@Valid @RequestBody EventoCalendarioRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        String runCreadorRef = ((UsuarioValidadoDto) request.getAttribute("usuarioAutenticado")).getRunUsuario();
        EventoCalendarioDto created = service.create(requestBody, runCreadorRef);
        return ResponseEntity.created(URI.create("/api/calendario/eventos/" + created.getId())).body(created);
    }

    @Operation(summary = "Actualizar evento")
    @PutMapping("/{id}")
    public ResponseEntity<EventoCalendarioDto> update(@PathVariable Long id, @Valid @RequestBody EventoCalendarioRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(service.update(id, requestBody));
    }

    @Operation(summary = "Eliminar evento")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void validarPermiso(HttpServletRequest request, String... rolesPermitidos) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        if (usuario == null || usuario.getRoles() == null || usuario.getRoles().stream().noneMatch(Arrays.asList(rolesPermitidos)::contains)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
    }
}
