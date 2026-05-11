package com.gestion.educativa.identidad.identidad.controller;

import java.util.List;
import com.gestion.educativa.identidad.identidad.models.dto.RolDto;
import com.gestion.educativa.identidad.identidad.models.request.AsignarRolRequest;
import com.gestion.educativa.identidad.identidad.services.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class RolController {

    private final RolService rolService;

    @PostMapping
    public ResponseEntity<RolDto> crearRol(@Valid @RequestBody RolDto rolDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crearRol(rolDto));
    }

    @GetMapping
    public ResponseEntity<List<RolDto>> listarRoles() {
        return ResponseEntity.ok(rolService.listarRoles());
    }

    @PostMapping("/asignar")
    public ResponseEntity<Void> asignarRol(@Valid @RequestBody AsignarRolRequest solicitud) {
        rolService.asignarRol(solicitud);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/revocar/{run}/{idRol}")
    public ResponseEntity<Void> revocarRol(@PathVariable String run, @PathVariable Integer idRol) {
        rolService.revocarRol(run, idRol);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{run}")
    public ResponseEntity<List<RolDto>> obtenerRolesPorUsuario(@PathVariable String run) {
        return ResponseEntity.ok(rolService.obtenerRolesPorUsuario(run));
    }
}
