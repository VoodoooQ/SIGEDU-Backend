package com.gestion.educativa.calendario.calendario.services.impl;

import com.gestion.educativa.calendario.calendario.models.dto.EventoCalendarioDto;
import com.gestion.educativa.calendario.calendario.models.entity.EventoCalendario;
import com.gestion.educativa.calendario.calendario.models.request.EventoCalendarioRequest;
import com.gestion.educativa.calendario.calendario.repositories.CalendarioRepository;
import com.gestion.educativa.calendario.calendario.services.CalendarioService;
import com.gestion.educativa.calendario.calendario.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CalendarioServiceImpl implements CalendarioService {

    private final CalendarioRepository repository;

    public CalendarioServiceImpl(CalendarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EventoCalendarioDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public EventoCalendarioDto findById(Long id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + id));
    }

    @Override
    public EventoCalendarioDto create(EventoCalendarioRequest request) {
        EventoCalendario e = new EventoCalendario();
        e.setNombre(request.getNombre());
        e.setDescripcion(request.getDescripcion());
        e.setFechaInicio(request.getFechaInicio());
        e.setFechaFin(request.getFechaFin());
        e.setHoraInicio(request.getHoraInicio());
        e.setHoraFin(request.getHoraFin());
        e.setTipo(request.getTipo());
        e.setUbicacion(request.getUbicacion());
        if (request.getActivo() != null) e.setActivo(request.getActivo());
        EventoCalendario saved = repository.save(e);
        return toDto(saved);
    }

    @Override
    public EventoCalendarioDto update(Long id, EventoCalendarioRequest request) {
        EventoCalendario existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + id));
        existing.setNombre(request.getNombre());
        existing.setDescripcion(request.getDescripcion());
        existing.setFechaInicio(request.getFechaInicio());
        existing.setFechaFin(request.getFechaFin());
        existing.setHoraInicio(request.getHoraInicio());
        existing.setHoraFin(request.getHoraFin());
        existing.setTipo(request.getTipo());
        existing.setUbicacion(request.getUbicacion());
        if (request.getActivo() != null) existing.setActivo(request.getActivo());
        EventoCalendario saved = repository.save(existing);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        EventoCalendario existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + id));
        repository.delete(existing);
    }

    @Override
    public List<EventoCalendarioDto> findByFecha(LocalDate fecha) {
        return repository.findByFechaInicio(fecha).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<EventoCalendarioDto> findByTipo(String tipo) {
        return repository.findByTipo(tipo).stream().map(this::toDto).collect(Collectors.toList());
    }

    private EventoCalendarioDto toDto(EventoCalendario e) {
        EventoCalendarioDto d = new EventoCalendarioDto();
        d.setId(e.getId());
        d.setNombre(e.getNombre());
        d.setDescripcion(e.getDescripcion());
        d.setFechaInicio(e.getFechaInicio());
        d.setFechaFin(e.getFechaFin());
        d.setHoraInicio(e.getHoraInicio());
        d.setHoraFin(e.getHoraFin());
        d.setTipo(e.getTipo());
        d.setUbicacion(e.getUbicacion());
        d.setActivo(e.isActivo());
        return d;
    }
}
