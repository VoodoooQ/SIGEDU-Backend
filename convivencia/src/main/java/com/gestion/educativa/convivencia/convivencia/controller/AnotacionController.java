package com.gestion.educativa.convivencia.convivencia.controller;

import com.gestion.educativa.convivencia.convivencia.models.dto.AnotacionesDto;
import com.gestion.educativa.convivencia.convivencia.models.request.AnotacionRequest;
import com.gestion.educativa.convivencia.convivencia.services.AnotacionesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/convivencia/anotaciones")
public class AnotacionController {

	private final AnotacionesService service;

	public AnotacionController(AnotacionesService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<AnotacionesDto>> list() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<AnotacionesDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@GetMapping("/estudiante/{estudianteId}")
	public ResponseEntity<List<AnotacionesDto>> getByEstudiante(@PathVariable Long estudianteId) {
		return ResponseEntity.ok(service.findByEstudianteId(estudianteId));
	}

	@PostMapping
	public ResponseEntity<AnotacionesDto> create(@Valid @RequestBody AnotacionRequest request) {
		AnotacionesDto created = service.create(request);
		return ResponseEntity.created(URI.create("/api/convivencia/anotaciones/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AnotacionesDto> update(@PathVariable Long id, @Valid @RequestBody AnotacionRequest request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
