package com.gestion.educativa.estructura.academica.services.impl;

import com.gestion.educativa.estructura.academica.models.dto.NivelDto;
import com.gestion.educativa.estructura.academica.models.entity.Nivel;
import com.gestion.educativa.estructura.academica.models.request.NivelRequest;
import com.gestion.educativa.estructura.academica.repositories.NivelRepository;
import com.gestion.educativa.estructura.academica.services.NivelService;
import com.gestion.educativa.estructura.academica.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NivelServiceImpl implements NivelService {

    private final NivelRepository repository;

    public NivelServiceImpl(NivelRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<NivelDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public NivelDto findById(Long id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel no encontrado con id: " + id));
    }

    @Override
    public NivelDto create(NivelRequest request) {
        Nivel n = new Nivel();
        n.setNombre(request.getNombre());
        n.setDescripcion(request.getDescripcion());
        if (request.getActivo() != null) n.setActivo(request.getActivo());
        n.setOrden(request.getOrden());
        Nivel saved = repository.save(n);
        return toDto(saved);
    }

    @Override
    public NivelDto update(Long id, NivelRequest request) {
        Nivel existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel no encontrado con id: " + id));
        existing.setNombre(request.getNombre());
        existing.setDescripcion(request.getDescripcion());
        if (request.getActivo() != null) existing.setActivo(request.getActivo());
        existing.setOrden(request.getOrden());
        Nivel saved = repository.save(existing);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Nivel existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel no encontrado con id: " + id));
        repository.delete(existing);
    }

    private NivelDto toDto(Nivel n) {
        NivelDto d = new NivelDto();
        d.setId(n.getId());
        d.setNombre(n.getNombre());
        d.setDescripcion(n.getDescripcion());
        d.setActivo(n.isActivo());
        d.setOrden(n.getOrden());
        return d;
    }
}

