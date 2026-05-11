package com.gestion.educativa.identidad.identidad.controller;

import java.util.List;
import com.gestion.educativa.identidad.identidad.models.dto.UsuarioDto;
import com.gestion.educativa.identidad.identidad.models.request.UsuarioRequest;
import com.gestion.educativa.identidad.identidad.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDto> crearUsuario(@Valid @RequestBody UsuarioRequest solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(solicitud));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{run}")
    public ResponseEntity<UsuarioDto> obtenerUsuario(@PathVariable String run) {
        return ResponseEntity.ok(usuarioService.obtenerUsuario(run));
    }

    @PutMapping("/{run}")
    public ResponseEntity<UsuarioDto> actualizarUsuario(
            @PathVariable String run,
            @Valid @RequestBody UsuarioRequest solicitud
    ) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(run, solicitud));
    }

    @DeleteMapping("/{run}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String run) {
        usuarioService.eliminarUsuario(run);
        return ResponseEntity.noContent().build();
    }
}
