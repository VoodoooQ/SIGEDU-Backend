package com.gestion.educativa.identidad.identidad.controller;

import java.util.List;
import com.gestion.educativa.identidad.identidad.models.dto.UsuarioDto;
import com.gestion.educativa.identidad.identidad.models.request.ActualizarUsuarioRequest;
import com.gestion.educativa.identidad.identidad.models.request.CrearUsuarioRequest;
import com.gestion.educativa.identidad.identidad.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIRECTIVO', 'INSPECTOR')")
    @Operation(summary = "Crear usuario", description = "Registra un nuevo usuario")
    @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    public ResponseEntity<UsuarioDto> crearUsuario(@Valid @RequestBody CrearUsuarioRequest solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(solicitud));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIRECTIVO', 'INSPECTOR', 'FUNCIONARIO', 'DOCENTE', 'APODERADO', 'ESTUDIANTE')")
    @Operation(summary = "Listar usuarios", description = "Obtiene usuarios segun permisos del solicitante")
    @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    public ResponseEntity<List<UsuarioDto>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{run}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIRECTIVO', 'INSPECTOR', 'FUNCIONARIO', 'DOCENTE', 'APODERADO', 'ESTUDIANTE')")
    @Operation(summary = "Obtener usuario", description = "Obtiene un usuario por RUN segun permisos")
    @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    public ResponseEntity<UsuarioDto> obtenerUsuario(@PathVariable String run) {
        return ResponseEntity.ok(usuarioService.obtenerUsuario(run));
    }

    @PutMapping("/{run}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIRECTIVO', 'INSPECTOR', 'FUNCIONARIO')")
    @Operation(summary = "Actualizar usuario", description = "Actualiza datos de usuario segun permisos")
    @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    public ResponseEntity<UsuarioDto> actualizarUsuario(
            @PathVariable String run,
            @Valid @RequestBody ActualizarUsuarioRequest solicitud
    ) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(run, solicitud));
    }

    @DeleteMapping("/{run}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIRECTIVO')")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por RUN")
    @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String run) {
        usuarioService.eliminarUsuario(run);
        return ResponseEntity.noContent().build();
    }
}
