package com.gestion.educativa.estructura.academica.controller;

import com.gestion.educativa.estructura.academica.models.dto.CursoDto;
import com.gestion.educativa.estructura.academica.models.request.CursoRequest;
import com.gestion.educativa.estructura.academica.services.CursoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/academica/cursos")
public class CursoController {

	private final CursoService service;

	public CursoController(CursoService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<CursoDto>> list() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CursoDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	public ResponseEntity<CursoDto> create(@Valid @RequestBody CursoRequest request) {
		CursoDto created = service.create(request);
		return ResponseEntity.created(URI.create("/api/academica/cursos/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CursoDto> update(@PathVariable Long id, @Valid @RequestBody CursoRequest request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}

