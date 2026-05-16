package com.gestion.educativa.calendario.calendario.controller;

import com.gestion.educativa.calendario.calendario.models.dto.EventoCalendarioDto;
import com.gestion.educativa.calendario.calendario.models.request.EventoCalendarioRequest;
import com.gestion.educativa.calendario.calendario.services.CalendarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendario/eventos")
public class CalendarioController {

	private final CalendarioService service;

	public CalendarioController(CalendarioService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<EventoCalendarioDto>> list() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EventoCalendarioDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@GetMapping("/fecha/{fecha}")
	public ResponseEntity<List<EventoCalendarioDto>> getByFecha(@PathVariable LocalDate fecha) {
		return ResponseEntity.ok(service.findByFecha(fecha));
	}

	@GetMapping("/tipo/{tipo}")
	public ResponseEntity<List<EventoCalendarioDto>> getByTipo(@PathVariable String tipo) {
		return ResponseEntity.ok(service.findByTipo(tipo));
	}

	@PostMapping
	public ResponseEntity<EventoCalendarioDto> create(@Valid @RequestBody EventoCalendarioRequest request) {
		EventoCalendarioDto created = service.create(request);
		return ResponseEntity.created(URI.create("/api/calendario/eventos/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EventoCalendarioDto> update(@PathVariable Long id, @Valid @RequestBody EventoCalendarioRequest request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
