package com.gestion.educativa.reuniones.reuniones.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.gestion.educativa.reuniones.reuniones.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.reuniones.reuniones.models.entity.BitacoraReunionApoderado;
import com.gestion.educativa.reuniones.reuniones.models.entity.BitacoraReunionGeneral;
import com.gestion.educativa.reuniones.reuniones.models.entity.BitacoraReunionP1aP1;
import com.gestion.educativa.reuniones.reuniones.services.ReunionService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/reuniones")
@Tag(name = "Reuniones")
public class ReunionController {

    private final ReunionService reunionService;

    public ReunionController(ReunionService reunionService) {
        this.reunionService = reunionService;
    }

    @Operation(summary = "Listar reuniones generales")
    @GetMapping("/generales")
    public ResponseEntity<List<BitacoraReunionGeneral>> listarGenerales(HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(reunionService.listarGenerales());
    }

    @Operation(summary = "Obtener reunión general por id")
    @GetMapping("/generales/{id}")
    public ResponseEntity<BitacoraReunionGeneral> obtenerGeneralPorId(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(reunionService.buscarGeneralPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reunion general no encontrada")));
    }

    @Operation(summary = "Crear reunión general")
    @PostMapping("/generales")
    public ResponseEntity<BitacoraReunionGeneral> crearGeneral(@RequestBody BitacoraReunionGeneral reunionGeneral, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.status(HttpStatus.CREATED).body(reunionService.guardarGeneral(reunionGeneral));
    }

    @Operation(summary = "Actualizar reunión general")
    @PutMapping("/generales/{id}")
    public ResponseEntity<BitacoraReunionGeneral> actualizarGeneral(@PathVariable Long id, @RequestBody BitacoraReunionGeneral reunionGeneral, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR");
        return ResponseEntity.ok(reunionService.actualizarGeneral(id, reunionGeneral));
    }

    @Operation(summary = "Eliminar reunión general")
    @DeleteMapping("/generales/{id}")
    public ResponseEntity<Void> eliminarGeneral(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        reunionService.eliminarGeneral(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar reuniones con apoderados")
    @GetMapping("/apoderados")
    public ResponseEntity<List<BitacoraReunionApoderado>> listarApoderados(HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "APODERADO");
        List<BitacoraReunionApoderado> reuniones = reunionService.listarApoderados();
        if (tieneRol(usuario, "APODERADO")) {
            reuniones = reuniones.stream()
                    .filter(reunion -> usuario.getRunUsuario() != null && usuario.getRunUsuario().equals(reunion.getRunApoderado()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(reuniones);
    }

    @Operation(summary = "Obtener reunión con apoderado por id")
    @GetMapping("/apoderados/{id}")
    public ResponseEntity<BitacoraReunionApoderado> obtenerApoderadoPorId(@PathVariable Long id, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "APODERADO");
        BitacoraReunionApoderado reunion = reunionService.buscarApoderadoPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reunion de apoderado no encontrada"));
        if (tieneRol(usuario, "APODERADO") && (usuario.getRunUsuario() == null || !usuario.getRunUsuario().equals(reunion.getRunApoderado()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
        return ResponseEntity.ok(reunion);
    }

    @Operation(summary = "Crear reunión con apoderado")
    @PostMapping("/apoderados")
    public ResponseEntity<BitacoraReunionApoderado> crearApoderado(@RequestBody BitacoraReunionApoderado reunionApoderado, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE");
        return ResponseEntity.status(HttpStatus.CREATED).body(reunionService.guardarApoderado(reunionApoderado));
    }

    @Operation(summary = "Actualizar reunión con apoderado")
    @PutMapping("/apoderados/{id}")
    public ResponseEntity<BitacoraReunionApoderado> actualizarApoderado(@PathVariable Long id, @RequestBody BitacoraReunionApoderado reunionApoderado, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE");
        return ResponseEntity.ok(reunionService.actualizarApoderado(id, reunionApoderado));
    }

    @Operation(summary = "Eliminar reunión con apoderado")
    @DeleteMapping("/apoderados/{id}")
    public ResponseEntity<Void> eliminarApoderado(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        reunionService.eliminarApoderado(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar reuniones P1A1")
    @GetMapping("/p1a1")
    public ResponseEntity<List<BitacoraReunionP1aP1>> listarP1aP1(HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "APODERADO");
        List<BitacoraReunionP1aP1> reuniones = reunionService.listarP1aP1();
        if (tieneRol(usuario, "APODERADO")) {
            reuniones = reuniones.stream()
                    .filter(reunion -> usuario.getRunUsuario() != null && usuario.getRunUsuario().equals(reunion.getRunEstudiante()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(reuniones);
    }

    @Operation(summary = "Obtener reunión P1A1 por id")
    @GetMapping("/p1a1/{id}")
    public ResponseEntity<BitacoraReunionP1aP1> obtenerP1aP1PorId(@PathVariable Long id, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "APODERADO");
        BitacoraReunionP1aP1 reunion = reunionService.buscarP1aP1PorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reunion p1a1 no encontrada"));
        if (tieneRol(usuario, "APODERADO") && (usuario.getRunUsuario() == null || !usuario.getRunUsuario().equals(reunion.getRunEstudiante()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
        return ResponseEntity.ok(reunion);
    }

    @Operation(summary = "Crear reunión P1A1")
    @PostMapping("/p1a1")
    public ResponseEntity<BitacoraReunionP1aP1> crearP1aP1(@RequestBody BitacoraReunionP1aP1 reunionP1aP1, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE");
        return ResponseEntity.status(HttpStatus.CREATED).body(reunionService.guardarP1aP1(reunionP1aP1));
    }

    @Operation(summary = "Actualizar reunión P1A1")
    @PutMapping("/p1a1/{id}")
    public ResponseEntity<BitacoraReunionP1aP1> actualizarP1aP1(@PathVariable Long id, @RequestBody BitacoraReunionP1aP1 reunionP1aP1, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE");
        return ResponseEntity.ok(reunionService.actualizarP1aP1(id, reunionP1aP1));
    }

    @Operation(summary = "Eliminar reunión P1A1")
    @DeleteMapping("/p1a1/{id}")
    public ResponseEntity<Void> eliminarP1aP1(@PathVariable Long id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        reunionService.eliminarP1aP1(id);
        return ResponseEntity.noContent().build();
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