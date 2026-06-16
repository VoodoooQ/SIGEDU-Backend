package com.gestion.educativa.geografia.geografia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;

import com.gestion.educativa.geografia.geografia.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.geografia.geografia.models.entity.Direccion;
import com.gestion.educativa.geografia.geografia.models.request.AgregarDireccion;
import com.gestion.educativa.geografia.geografia.models.request.ModificarDireccion;
import com.gestion.educativa.geografia.geografia.services.DireccionService;
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

@RequestMapping("/api/direcciones")
@RestController
@Tag(name = "Direcciones")
public class DireccionController {
    @Autowired
    private DireccionService direccionService;

    @Operation(summary = "Agregar direcci?n")
    @PostMapping
    public ResponseEntity<String> crearDireccion(@Valid @RequestBody AgregarDireccion direccionRequest, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        String rutUsuario = usuario.getRunUsuario();
        if (rutUsuario == null || rutUsuario.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El run del usuario es obligatorio");
        }

        direccionService.agregarDireccion(direccionRequest, rutUsuario);
        return ResponseEntity.ok("Direccion creada exitosamente");
    }

    @Operation(summary = "Modificar direcci?n")
    @PutMapping("/modificar/{id}")
    public ResponseEntity<String> modificarDireccion(@PathVariable int id, @Valid @RequestBody ModificarDireccion direccionRequest, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        Direccion direccion = direccionService.obtenerDireccionPorId(id);
        boolean esAdministrador = tieneRol(usuario, "DIRECTIVO", "ADMIN", "INSPECTOR");
        boolean esDueno = usuario.getRunUsuario() != null && usuario.getRunUsuario().equals(direccion.getRun_usuario_ref());

        if (!esAdministrador && !esDueno) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }

        direccionService.modificarDireccion(id, direccionRequest);
        return ResponseEntity.ok("Direccion modificada exitosamente");
    }

    @Operation(summary = "Listar direcciones por usuario")
    @GetMapping("{rut_usuario}")
    public ResponseEntity<List<Direccion>> obtenerDireccionPorRun(@PathVariable String rut_usuario, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE", "ESTUDIANTE", "APODERADO");
        List<Direccion> direcciones = direccionService.obtenerDireccionPorRun(rut_usuario);
        return ResponseEntity.ok(direcciones);
    }

    @Operation(summary = "Eliminar direcci?n")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDireccion(@PathVariable int id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        direccionService.eliminarDireccion(id);
        return ResponseEntity.ok("Direccion eliminada exitosamente");
    }

    private UsuarioValidadoDto obtenerUsuario(HttpServletRequest request) {
        return (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
    }

    private void validarPermiso(HttpServletRequest request, String... rolesPermitidos) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        if (usuario == null || usuario.getRoles() == null || usuario.getRoles().stream().noneMatch(Arrays.asList(rolesPermitidos)::contains)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
    }

    private boolean tieneRol(UsuarioValidadoDto usuario, String... rolesPermitidos) {
        return usuario != null
                && usuario.getRoles() != null
                && usuario.getRoles().stream().anyMatch(Arrays.asList(rolesPermitidos)::contains);
    }
}
