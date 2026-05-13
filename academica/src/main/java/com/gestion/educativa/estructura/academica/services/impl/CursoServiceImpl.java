package com.gestion.educativa.estructura.academica.services.impl;

import com.gestion.educativa.estructura.academica.models.dto.CursoDto;
import com.gestion.educativa.estructura.academica.models.entity.Curso;
import com.gestion.educativa.estructura.academica.models.request.CursoRequest;
import com.gestion.educativa.estructura.academica.repositories.CursoRepository;
import com.gestion.educativa.estructura.academica.services.CursoService;
import com.gestion.educativa.estructura.academica.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CursoServiceImpl implements CursoService {

    private final CursoRepository repository;

    public CursoServiceImpl(CursoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CursoDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public CursoDto findById(Long id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + id));
    }

    @Override
    public CursoDto create(CursoRequest request) {
        Curso c = new Curso();
        c.setNombre(request.getNombre());
        c.setDescripcion(request.getDescripcion());
        c.setNivelId(request.getNivelId());
        c.setPeriodoId(request.getPeriodoId());
        c.setSalaId(request.getSalaId());
        if (request.getActivo() != null) c.setActivo(request.getActivo());
        Curso saved = repository.save(c);
        return toDto(saved);
    }

    @Override
    public CursoDto update(Long id, CursoRequest request) {
        Curso existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + id));
        existing.setNombre(request.getNombre());
        existing.setDescripcion(request.getDescripcion());
        existing.setNivelId(request.getNivelId());
        existing.setPeriodoId(request.getPeriodoId());
        existing.setSalaId(request.getSalaId());
        if (request.getActivo() != null) existing.setActivo(request.getActivo());
        Curso saved = repository.save(existing);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Curso existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + id));
        repository.delete(existing);
    }

    private CursoDto toDto(Curso c) {
        CursoDto d = new CursoDto();
        d.setId(c.getId());
        d.setNombre(c.getNombre());
        d.setDescripcion(c.getDescripcion());
        d.setNivelId(c.getNivelId());
        d.setPeriodoId(c.getPeriodoId());
        d.setSalaId(c.getSalaId());
        d.setActivo(c.isActivo());
        return d;
    }
}
