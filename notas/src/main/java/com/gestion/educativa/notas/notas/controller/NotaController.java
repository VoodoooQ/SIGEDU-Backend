package com.gestion.educativa.notas.notas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import com.gestion.educativa.notas.notas.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.notas.notas.models.entity.Nota;
import com.gestion.educativa.notas.notas.models.request.NotaRequest;
import com.gestion.educativa.notas.notas.services.NotaService;
import com.gestion.educativa.notas.notas.services.IdentidadClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/api/notas")
@Tag(name = "Notas")
public class NotaController {

    private final NotaService notaService;
    private final IdentidadClientService identidadClientService;

    public NotaController(NotaService notaService, IdentidadClientService identidadClientService) {
        this.notaService = notaService;
        this.identidadClientService = identidadClientService;
    }

    @Operation(summary = "Listar notas")
    @GetMapping
    public ResponseEntity<List<Nota>> listar(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(notaService.listar());
    }

    @Operation(summary = "Listar notas por estudiante")
    @GetMapping("/estudiante/{runEstudiante}")
    public ResponseEntity<List<Nota>> listarPorEstudiante(@PathVariable String runEstudiante, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "APODERADO", "ESTUDIANTE");
        validarAccesoEstudiante(request, usuario, runEstudiante);
        return ResponseEntity.ok(notaService.listarPorEstudiante(runEstudiante));
    }

    @Operation(summary = "Crear nota")
    @PostMapping
    public ResponseEntity<Nota> crear(@Valid @RequestBody NotaRequest requestBody, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "DOCENTE");
        String runDocenteRef = obtenerUsuario(request).getRunUsuario();
        return ResponseEntity.status(HttpStatus.CREATED).body(notaService.crear(requestBody, runDocenteRef));
    }

    @Operation(summary = "Actualizar nota")
    @PutMapping("/{id}")
    public ResponseEntity<Nota> actualizar(@PathVariable Long id, @Valid @RequestBody NotaRequest requestBody, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        Nota nota = notaService.obtenerPorId(id);
        boolean puedeActualizar = tieneRol(usuario, "ADMIN", "DIRECTIVO")
                || (tieneRol(usuario, "DOCENTE") && usuario.getRunUsuario() != null && usuario.getRunUsuario().equals(nota.getRunDocenteRef()));
        if (!puedeActualizar) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
        return ResponseEntity.ok(notaService.actualizar(id, requestBody));
    }

    @Operation(summary = "Eliminar nota")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        notaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    private void validarAccesoEstudiante(HttpServletRequest request, UsuarioValidadoDto usuario, String runEstudiante) {
        if (tieneRol(usuario, "ESTUDIANTE") && !normalizarRun(usuario.getRunUsuario()).equals(normalizarRun(runEstudiante))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
        if (tieneRol(usuario, "APODERADO")
                && !identidadClientService.estudianteVinculadoAlApoderado(request.getHeader(HttpHeaders.AUTHORIZATION), runEstudiante)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
    }

    private String normalizarRun(String run) {
        return run == null ? "" : run.replaceAll("[^0-9]", "").trim();
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
