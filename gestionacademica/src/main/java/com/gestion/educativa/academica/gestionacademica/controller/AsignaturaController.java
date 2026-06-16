package com.gestion.educativa.academica.gestionacademica.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;

import com.gestion.educativa.academica.gestionacademica.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.academica.gestionacademica.models.entity.Asignatura;
import com.gestion.educativa.academica.gestionacademica.models.request.Agregar_Modificar_Asignatura;
import com.gestion.educativa.academica.gestionacademica.services.AsignaturaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

@RequestMapping("/api/asignatura")
@RestController
@Tag(name = "Asignaturas")
public class AsignaturaController {
    @Autowired
    private AsignaturaService asignaturaService;

    @Operation(summary = "Listar asignaturas")
    @GetMapping
    public ResponseEntity<List<Asignatura>> obtenerAsignaturas(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "DOCENTE", "INSPECTOR", "ESTUDIANTE", "APODERADO");
        List<Asignatura> asignaturas = asignaturaService.obtenerAsignaturas();
        if (asignaturas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(asignaturas);
    }

    @Operation(summary = "Obtener asignatura por id")
    @GetMapping("{id}")
    public ResponseEntity<Asignatura> obtenerAsignaturaPorId(@PathVariable int id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "DOCENTE", "INSPECTOR", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(asignaturaService.obtenerAsignaturaPorId(id));
    }

    @Operation(summary = "Crear asignatura")
    @PostMapping
    public ResponseEntity<Asignatura> agregarAsignatura(@Valid @RequestBody Agregar_Modificar_Asignatura entity, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        Asignatura nuevaAsignatura = asignaturaService.agregarAsignatura(entity);
        return ResponseEntity.status(201).body(nuevaAsignatura);
    }

    @Operation(summary = "Modificar asignatura")
    @PutMapping("/modificar/{id}")
    public ResponseEntity<Asignatura> modificarAsignatura(@PathVariable int id, @Valid @RequestBody Agregar_Modificar_Asignatura entity, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        Asignatura asignatura = asignaturaService.obtenerAsignaturaPorId(id);
        boolean puedeModificar = tieneRol(usuario, "ADMIN", "DIRECTIVO")
                || (tieneRol(usuario, "DOCENTE") && usuario.getRunUsuario() != null && usuario.getRunUsuario().equals(asignatura.getRun_docente_ref()));
        if (!puedeModificar) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
        Asignatura asignaturaActualizada = asignaturaService.modificarAsignatura(id, entity);
        return ResponseEntity.ok(asignaturaActualizada);
    }

    @Operation(summary = "Eliminar asignatura")
    @DeleteMapping("{id}")
    public ResponseEntity<String> eliminarAsignatura(@PathVariable int id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        String mensaje = asignaturaService.eliminarAsignatura(id);
        return ResponseEntity.ok(mensaje);
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
        return usuario != null
                && usuario.getRoles() != null
                && usuario.getRoles().stream().anyMatch(Arrays.asList(rolesPermitidos)::contains);
    }
}
