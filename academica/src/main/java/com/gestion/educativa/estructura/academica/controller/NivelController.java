package com.gestion.educativa.estructura.academica.controller;

import com.gestion.educativa.estructura.academica.models.dto.NivelDto;
import com.gestion.educativa.estructura.academica.models.request.NivelRequest;
import com.gestion.educativa.estructura.academica.services.NivelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/academica/niveles")
public class NivelController {

	private final NivelService service;

	public NivelController(NivelService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<NivelDto>> list() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<NivelDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	public ResponseEntity<NivelDto> create(@Valid @RequestBody NivelRequest request) {
		NivelDto created = service.create(request);
		return ResponseEntity.created(URI.create("/api/academica/niveles/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<NivelDto> update(@PathVariable Long id, @Valid @RequestBody NivelRequest request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
