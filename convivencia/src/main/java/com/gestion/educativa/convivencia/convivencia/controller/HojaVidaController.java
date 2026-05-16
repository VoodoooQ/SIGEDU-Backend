package com.gestion.educativa.convivencia.convivencia.controller;

import com.gestion.educativa.convivencia.convivencia.models.dto.HojaVidaEstudianteDto;
import com.gestion.educativa.convivencia.convivencia.models.request.HojaVidaRequest;
import com.gestion.educativa.convivencia.convivencia.services.HojaVidaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/convivencia/hoja-vida")
public class HojaVidaController {

	private final HojaVidaService service;

	public HojaVidaController(HojaVidaService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<HojaVidaEstudianteDto>> list() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<HojaVidaEstudianteDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@GetMapping("/estudiante/{estudianteId}")
	public ResponseEntity<List<HojaVidaEstudianteDto>> getByEstudiante(@PathVariable Long estudianteId) {
		return ResponseEntity.ok(service.findByEstudianteId(estudianteId));
	}

	@PostMapping
	public ResponseEntity<HojaVidaEstudianteDto> create(@Valid @RequestBody HojaVidaRequest request) {
		HojaVidaEstudianteDto created = service.create(request);
		return ResponseEntity.created(URI.create("/api/convivencia/hoja-vida/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<HojaVidaEstudianteDto> update(@PathVariable Long id, @Valid @RequestBody HojaVidaRequest request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
