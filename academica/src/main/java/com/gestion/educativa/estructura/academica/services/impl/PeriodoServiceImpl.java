package com.gestion.educativa.estructura.academica.services.impl;

import com.gestion.educativa.estructura.academica.models.dto.PeriodoDto;
import com.gestion.educativa.estructura.academica.models.entity.Periodo;
import com.gestion.educativa.estructura.academica.models.request.PeriodoRequest;
import com.gestion.educativa.estructura.academica.repositories.PeriodoRepository;
import com.gestion.educativa.estructura.academica.services.PeriodoService;
import com.gestion.educativa.estructura.academica.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PeriodoServiceImpl implements PeriodoService {

    private final PeriodoRepository repository;

    public PeriodoServiceImpl(PeriodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PeriodoDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public PeriodoDto findById(Long id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo no encontrado con id: " + id));
    }

    @Override
    public PeriodoDto create(PeriodoRequest request) {
        Periodo p = new Periodo();
        p.setNombre(request.getNombre());
        p.setFechaInicio(request.getFechaInicio());
        p.setFechaFin(request.getFechaFin());
        if (request.getActivo() != null) p.setActivo(request.getActivo());
        Periodo saved = repository.save(p);
        return toDto(saved);
    }

    @Override
    public PeriodoDto update(Long id, PeriodoRequest request) {
        Periodo existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo no encontrado con id: " + id));
        existing.setNombre(request.getNombre());
        existing.setFechaInicio(request.getFechaInicio());
        existing.setFechaFin(request.getFechaFin());
        if (request.getActivo() != null) existing.setActivo(request.getActivo());
        Periodo saved = repository.save(existing);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Periodo existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo no encontrado con id: " + id));
        repository.delete(existing);
    }

    private PeriodoDto toDto(Periodo p) {
        PeriodoDto d = new PeriodoDto();
        d.setId(p.getId());
        d.setNombre(p.getNombre());
        d.setFechaInicio(p.getFechaInicio());
        d.setFechaFin(p.getFechaFin());
        d.setActivo(p.isActivo());
        return d;
    }
}
