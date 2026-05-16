package com.gestion.educativa.convivencia.convivencia.services.impl;

import com.gestion.educativa.convivencia.convivencia.models.dto.HojaVidaEstudianteDto;
import com.gestion.educativa.convivencia.convivencia.models.entity.HojaVidaEstudiante;
import com.gestion.educativa.convivencia.convivencia.models.request.HojaVidaRequest;
import com.gestion.educativa.convivencia.convivencia.repositories.HojaVidaEstudianteRepository;
import com.gestion.educativa.convivencia.convivencia.services.HojaVidaService;
import com.gestion.educativa.convivencia.convivencia.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HojaVidaServiceImpl implements HojaVidaService {

    private final HojaVidaEstudianteRepository repository;

    public HojaVidaServiceImpl(HojaVidaEstudianteRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<HojaVidaEstudianteDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public HojaVidaEstudianteDto findById(Long id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada con id: " + id));
    }

    @Override
    public HojaVidaEstudianteDto create(HojaVidaRequest request) {
        HojaVidaEstudiante h = new HojaVidaEstudiante();
        h.setEstudianteId(request.getEstudianteId());
        h.setComportamiento(request.getComportamiento());
        h.setAsistencia(request.getAsistencia());
        h.setNovedades(request.getNovedades());
        h.setFechaRegistro(request.getFechaRegistro());
        h.setRegistradoPor(request.getRegistradoPor());
        HojaVidaEstudiante saved = repository.save(h);
        return toDto(saved);
    }

    @Override
    public HojaVidaEstudianteDto update(Long id, HojaVidaRequest request) {
        HojaVidaEstudiante existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada con id: " + id));
        existing.setEstudianteId(request.getEstudianteId());
        existing.setComportamiento(request.getComportamiento());
        existing.setAsistencia(request.getAsistencia());
        existing.setNovedades(request.getNovedades());
        existing.setFechaRegistro(request.getFechaRegistro());
        existing.setRegistradoPor(request.getRegistradoPor());
        HojaVidaEstudiante saved = repository.save(existing);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        HojaVidaEstudiante existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada con id: " + id));
        repository.delete(existing);
    }

    @Override
    public List<HojaVidaEstudianteDto> findByEstudianteId(Long estudianteId) {
        return repository.findByEstudianteId(estudianteId).stream().map(this::toDto).collect(Collectors.toList());
    }

    private HojaVidaEstudianteDto toDto(HojaVidaEstudiante h) {
        HojaVidaEstudianteDto d = new HojaVidaEstudianteDto();
        d.setId(h.getId());
        d.setEstudianteId(h.getEstudianteId());
        d.setComportamiento(h.getComportamiento());
        d.setAsistencia(h.getAsistencia());
        d.setNovedades(h.getNovedades());
        d.setFechaRegistro(h.getFechaRegistro());
        d.setRegistradoPor(h.getRegistradoPor());
        return d;
    }
}
