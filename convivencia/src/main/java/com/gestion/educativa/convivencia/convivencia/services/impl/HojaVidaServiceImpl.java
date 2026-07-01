package com.gestion.educativa.convivencia.convivencia.services.impl;

import com.gestion.educativa.convivencia.convivencia.exceptions.ResourceNotFoundException;
import com.gestion.educativa.convivencia.convivencia.models.dto.HojaVidaEstudianteDto;
import com.gestion.educativa.convivencia.convivencia.models.entity.HojaVidaEstudiante;
import com.gestion.educativa.convivencia.convivencia.models.request.HojaVidaRequest;
import com.gestion.educativa.convivencia.convivencia.repositories.HojaVidaEstudianteRepository;
import com.gestion.educativa.convivencia.convivencia.services.HojaVidaService;
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
    public List<HojaVidaEstudianteDto> findAll() { return repository.findAll().stream().map(this::toDto).collect(Collectors.toList()); }

    @Override
    public HojaVidaEstudianteDto findById(Long id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada con id: " + id));
    }

    @Override
    public HojaVidaEstudianteDto create(HojaVidaRequest request, String runAutorRef) {
        HojaVidaEstudiante h = new HojaVidaEstudiante();
        h.setRunEstudianteRef(request.getRunEstudianteRef());
        h.setComportamiento(request.getComportamiento());
        h.setAsistencia(request.getAsistencia());
        h.setNovedades(request.getNovedades());
        h.setFechaRegistro(request.getFechaRegistro());
        h.setRunAutorRef(runAutorRef);
        return toDto(repository.save(h));
    }

    @Override
    public HojaVidaEstudianteDto update(Long id, HojaVidaRequest request) {
        HojaVidaEstudiante existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada con id: " + id));
        existing.setRunEstudianteRef(request.getRunEstudianteRef());
        existing.setComportamiento(request.getComportamiento());
        existing.setAsistencia(request.getAsistencia());
        existing.setNovedades(request.getNovedades());
        existing.setFechaRegistro(request.getFechaRegistro());
        return toDto(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        HojaVidaEstudiante existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada con id: " + id));
        repository.delete(existing);
    }

    @Override
    public List<HojaVidaEstudianteDto> findByRunEstudianteRef(String runEstudianteRef) {
        return repository.findByRunEstudianteRef(runEstudianteRef).stream().map(this::toDto).collect(Collectors.toList());
    }

    private HojaVidaEstudianteDto toDto(HojaVidaEstudiante h) {
        HojaVidaEstudianteDto d = new HojaVidaEstudianteDto();
        d.setId(h.getId());
        d.setRunEstudianteRef(h.getRunEstudianteRef());
        d.setComportamiento(h.getComportamiento());
        d.setAsistencia(h.getAsistencia());
        d.setNovedades(h.getNovedades());
        d.setFechaRegistro(h.getFechaRegistro());
        d.setRunAutorRef(h.getRunAutorRef());
        return d;
    }
}