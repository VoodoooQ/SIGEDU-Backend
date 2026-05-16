package com.gestion.educativa.estructura.academica.controller;

import com.gestion.educativa.estructura.academica.models.dto.SalaDto;
import com.gestion.educativa.estructura.academica.models.request.SalaRequest;
import com.gestion.educativa.estructura.academica.services.SalaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/academica/salas")
public class SalaController {

	private final SalaService service;

	public SalaController(SalaService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<SalaDto>> list() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SalaDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	public ResponseEntity<SalaDto> create(@Valid @RequestBody SalaRequest request) {
		SalaDto created = service.create(request);
		return ResponseEntity.created(URI.create("/api/academica/salas/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<SalaDto> update(@PathVariable Long id, @Valid @RequestBody SalaRequest request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
