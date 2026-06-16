package com.gestion.educativa.calendario.calendario.services;

import com.gestion.educativa.calendario.calendario.models.dto.EventoCalendarioDto;
import com.gestion.educativa.calendario.calendario.models.request.EventoCalendarioRequest;

import java.time.LocalDate;
import java.util.List;

public interface CalendarioService {
    List<EventoCalendarioDto> findAll();
    EventoCalendarioDto findById(Long id);
    EventoCalendarioDto create(EventoCalendarioRequest request, String runCreadorRef);
    EventoCalendarioDto update(Long id, EventoCalendarioRequest request);
    void delete(Long id);
    List<EventoCalendarioDto> findByFecha(LocalDate fecha);
    List<EventoCalendarioDto> findByTipo(String tipo);
}