package com.gestion.educativa.academica.gestionacademica.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;

import com.gestion.educativa.academica.gestionacademica.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.academica.gestionacademica.models.entity.ObjetivosAprendizaje;
import com.gestion.educativa.academica.gestionacademica.models.request.AgregarObjetivo;
import com.gestion.educativa.academica.gestionacademica.services.ObjetivoAprendizajeService;
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

@RequestMapping("/api/objetivos")
@RestController
@Tag(name = "Objetivos de Aprendizaje")
public class ObjetivosController {
    @Autowired
    private ObjetivoAprendizajeService objetivoAprendizajeService;

    @Operation(summary = "Listar objetivos por asignatura")
    @GetMapping("/asignatura/{idAsignatura}")
    public ResponseEntity<List<ObjetivosAprendizaje>> obtenerObjetivosPorAsignatura(@PathVariable int idAsignatura, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "DOCENTE", "INSPECTOR", "ESTUDIANTE", "APODERADO");
        return ResponseEntity.ok(objetivoAprendizajeService.obtenerObjetivosPorAsignatura(idAsignatura));
    }

    @Operation(summary = "Crear objetivo de aprendizaje")
    @PostMapping
    public ResponseEntity<ObjetivosAprendizaje> agregarObjetivoAprendizaje(@Valid @RequestBody AgregarObjetivo entity, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "DOCENTE");
        ObjetivosAprendizaje nuevoObjetivo = objetivoAprendizajeService.agregarObjetivosAprendizaje(entity);
        return ResponseEntity.status(201).body(nuevoObjetivo);
    }

    @Operation(summary = "Modificar objetivo")
    @PutMapping("{id}")
    public ResponseEntity<ObjetivosAprendizaje> modificarObjetivoAprendizaje(@PathVariable int id, @Valid @RequestBody AgregarObjetivo entity, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO", "DOCENTE");
        return ResponseEntity.ok(objetivoAprendizajeService.modificarObjetivo(id, entity));
    }

    @Operation(summary = "Eliminar objetivo")
    @DeleteMapping("{id}")
    public ResponseEntity<String> eliminarObjetivoAprendizaje(@PathVariable int id, HttpServletRequest request) {
        validarPermiso(request, "ADMIN", "DIRECTIVO");
        return ResponseEntity.ok(objetivoAprendizajeService.eliminarObjetivo(id));
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
}
