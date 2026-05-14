package com.gestion.educativa.identidad.identidad.controller;

import java.util.List;
import com.gestion.educativa.identidad.identidad.models.dto.RolDto;
import com.gestion.educativa.identidad.identidad.models.request.AsignarRolRequest;
import com.gestion.educativa.identidad.identidad.services.RolService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
@Tag(name = "Roles")
public class RolController {

    private final RolService rolService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIRECTIVO')")
    @Operation(summary = "Crear rol", description = "Crea un nuevo rol")
    @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    public ResponseEntity<RolDto> crearRol(@Valid @RequestBody RolDto rolDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crearRol(rolDto));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIRECTIVO')")
    @Operation(summary = "Listar roles", description = "Obtiene el listado de roles")
    @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    public ResponseEntity<List<RolDto>> listarRoles() {
        return ResponseEntity.ok(rolService.listarRoles());
    }

    @PostMapping("/asignar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIRECTIVO')")
    @Operation(summary = "Asignar rol", description = "Asigna un rol a un usuario")
    @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    public ResponseEntity<Void> asignarRol(@Valid @RequestBody AsignarRolRequest solicitud) {
        rolService.asignarRol(solicitud);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/revocar/{run}/{idRol}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIRECTIVO')")
    @Operation(summary = "Revocar rol", description = "Revoca un rol a un usuario")
    @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    public ResponseEntity<Void> revocarRol(@PathVariable String run, @PathVariable Integer idRol) {
        rolService.revocarRol(run, idRol);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{run}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIRECTIVO')")
    @Operation(summary = "Roles por usuario", description = "Obtiene roles asignados a un usuario")
    @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    public ResponseEntity<List<RolDto>> obtenerRolesPorUsuario(@PathVariable String run) {
        return ResponseEntity.ok(rolService.obtenerRolesPorUsuario(run));
    }
}
