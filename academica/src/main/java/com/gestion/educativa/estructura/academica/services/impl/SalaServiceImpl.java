package com.gestion.educativa.estructura.academica.services.impl;

import com.gestion.educativa.estructura.academica.models.dto.SalaDto;
import com.gestion.educativa.estructura.academica.models.entity.Sala;
import com.gestion.educativa.estructura.academica.models.request.SalaRequest;
import com.gestion.educativa.estructura.academica.repositories.SalaRepository;
import com.gestion.educativa.estructura.academica.services.SalaService;
import com.gestion.educativa.estructura.academica.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SalaServiceImpl implements SalaService {

    private final SalaRepository repository;

    public SalaServiceImpl(SalaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SalaDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public SalaDto findById(Long id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + id));
    }

    @Override
    public SalaDto create(SalaRequest request) {
        Sala s = new Sala();
        s.setNombre(request.getNombre());
        s.setDescripcion(request.getDescripcion());
        s.setCapacidad(request.getCapacidad());
        if (request.getActivo() != null) s.setActivo(request.getActivo());
        Sala saved = repository.save(s);
        return toDto(saved);
    }

    @Override
    public SalaDto update(Long id, SalaRequest request) {
        Sala existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + id));
        existing.setNombre(request.getNombre());
        existing.setDescripcion(request.getDescripcion());
        existing.setCapacidad(request.getCapacidad());
        if (request.getActivo() != null) existing.setActivo(request.getActivo());
        Sala saved = repository.save(existing);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Sala existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + id));
        repository.delete(existing);
    }

    private SalaDto toDto(Sala s) {
        SalaDto d = new SalaDto();
        d.setId(s.getId());
        d.setNombre(s.getNombre());
        d.setDescripcion(s.getDescripcion());
        d.setCapacidad(s.getCapacidad());
        d.setActivo(s.isActivo());
        return d;
    }
}
