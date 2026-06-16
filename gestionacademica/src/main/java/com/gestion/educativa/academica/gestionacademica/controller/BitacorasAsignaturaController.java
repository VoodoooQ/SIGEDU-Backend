package com.gestion.educativa.academica.gestionacademica.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;

import com.gestion.educativa.academica.gestionacademica.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.academica.gestionacademica.models.entity.BitacoraAsignatura;
import com.gestion.educativa.academica.gestionacademica.models.request.AgregarBitacora;
import com.gestion.educativa.academica.gestionacademica.models.request.ModificarBitacora;
import com.gestion.educativa.academica.gestionacademica.services.BitacoraAsignaturaService;
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

@RequestMapping("/api/bitacora")
@RestController
@Tag(name = "Bit?coras")
public class BitacorasAsignaturaController {
    @Autowired
    private BitacoraAsignaturaService bitacoraAsignaturaService;

    @Operation(summary = "Listar bit?coras por asignatura")
    @GetMapping("/asignatura/{idAsignatura}")
    public ResponseEntity<List<BitacoraAsignatura>> obtenerBitacorasPorAsignatura(@PathVariable int idAsignatura, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE");
        return ResponseEntity.ok(bitacoraAsignaturaService.obtenerBitacorasPorAsignatura(idAsignatura));
    }

    @Operation(summary = "Registrar bit?cora")
    @PostMapping
    public ResponseEntity<BitacoraAsignatura> registrarBitacoraAsignatura(@Valid @RequestBody AgregarBitacora entity, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "DOCENTE");
        String runDocente = obtenerUsuario(request).getRunUsuario();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bitacoraAsignaturaService.registrarBitacoraAsignatura(entity, runDocente));
    }

    @Operation(summary = "Modificar bit?cora")
    @PutMapping("{id}")
    public ResponseEntity<BitacoraAsignatura> modificarBitacoraAsignatura(@PathVariable int id, @Valid @RequestBody ModificarBitacora entity, HttpServletRequest request) {
        UsuarioValidadoDto usuario = obtenerUsuario(request);
        BitacoraAsignatura bitacora = bitacoraAsignaturaService.obtenerBitacoraPorId(id);
        boolean puedeModificar = tieneRol(usuario, "ADMIN", "DIRECTIVO")
                || (tieneRol(usuario, "DOCENTE") && usuario.getRunUsuario() != null && usuario.getRunUsuario().equals(bitacora.getRun_docente_ref()));
        if (!puedeModificar) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para esta accion");
        }
        BitacoraAsignatura bitacoraActualizada = bitacoraAsignaturaService.modificarBitacoraAsignatura(id, entity);
        return ResponseEntity.ok(bitacoraActualizada);
    }

    @Operation(summary = "Eliminar bit?cora")
    @DeleteMapping("{id}")
    public ResponseEntity<String> eliminarBitacoraAsignatura(@PathVariable int id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        String mensaje = bitacoraAsignaturaService.eliminarBitacoraAsignatura(id);
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
