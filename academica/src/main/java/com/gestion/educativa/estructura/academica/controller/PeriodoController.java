package com.gestion.educativa.estructura.academica.controller;

import com.gestion.educativa.estructura.academica.models.dto.PeriodoDto;
import com.gestion.educativa.estructura.academica.models.request.PeriodoRequest;
import com.gestion.educativa.estructura.academica.services.PeriodoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/academica/periodos")
public class PeriodoController {

	private final PeriodoService service;

	public PeriodoController(PeriodoService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<PeriodoDto>> list() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<PeriodoDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	public ResponseEntity<PeriodoDto> create(@Valid @RequestBody PeriodoRequest request) {
		PeriodoDto created = service.create(request);
		return ResponseEntity.created(URI.create("/api/academica/periodos/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PeriodoDto> update(@PathVariable Long id, @Valid @RequestBody PeriodoRequest request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
