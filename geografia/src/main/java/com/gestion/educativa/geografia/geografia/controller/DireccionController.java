package com.gestion.educativa.geografia.geografia.controller;

import java.security.Principal;
import java.util.List;
import com.gestion.educativa.geografia.geografia.models.entity.Direccion;
import com.gestion.educativa.geografia.geografia.models.request.AgregarDireccion;
import com.gestion.educativa.geografia.geografia.models.request.ModificarDireccion;
import com.gestion.educativa.geografia.geografia.services.DireccionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class DireccionController {
    @Autowired
    private DireccionService direccionService;

    @PostMapping
    public ResponseEntity<String> crearDireccion(@Valid @RequestBody AgregarDireccion direccionRequest, Principal principal) {
        String rutUsuario = principal != null ? principal.getName() : direccionRequest.getRun_usuario_ref();
        if (rutUsuario == null || rutUsuario.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El run del usuario es obligatorio");
        }

        direccionService.agregarDireccion(direccionRequest, rutUsuario);
        return ResponseEntity.ok("Direccion creada exitosamente");
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<String> modificarDireccion(@PathVariable int id, @Valid @RequestBody ModificarDireccion direccionRequest) {
        direccionService.modificarDireccion(id, direccionRequest);
        return ResponseEntity.ok("Direccion modificada exitosamente");
    }

    @GetMapping("{rut_usuario}")
    public ResponseEntity<List<Direccion>> obtenerDireccionPorRun(@PathVariable String rut_usuario) {
        List<Direccion> direcciones = direccionService.obtenerDireccionPorRun(rut_usuario);
        return ResponseEntity.ok(direcciones);
    }
}
