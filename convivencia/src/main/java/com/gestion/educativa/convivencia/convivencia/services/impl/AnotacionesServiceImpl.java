package com.gestion.educativa.convivencia.convivencia.services.impl;

import com.gestion.educativa.convivencia.convivencia.models.dto.AnotacionesDto;
import com.gestion.educativa.convivencia.convivencia.models.entity.Anotaciones;
import com.gestion.educativa.convivencia.convivencia.models.request.AnotacionRequest;
import com.gestion.educativa.convivencia.convivencia.repositories.AnotacionesRepository;
import com.gestion.educativa.convivencia.convivencia.services.AnotacionesService;
import com.gestion.educativa.convivencia.convivencia.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnotacionesServiceImpl implements AnotacionesService {

    private final AnotacionesRepository repository;

    public AnotacionesServiceImpl(AnotacionesRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AnotacionesDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public AnotacionesDto findById(Long id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Anotación no encontrada con id: " + id));
    }

    @Override
    public AnotacionesDto create(AnotacionRequest request) {
        Anotaciones a = new Anotaciones();
        a.setEstudianteId(request.getEstudianteId());
        a.setFecha(request.getFecha());
        a.setTipo(request.getTipo());
        a.setDescripcion(request.getDescripcion());
        a.setRegistradoPor(request.getRegistradoPor());
        Anotaciones saved = repository.save(a);
        return toDto(saved);
    }

    @Override
    public AnotacionesDto update(Long id, AnotacionRequest request) {
        Anotaciones existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotación no encontrada con id: " + id));
        existing.setEstudianteId(request.getEstudianteId());
        existing.setFecha(request.getFecha());
        existing.setTipo(request.getTipo());
        existing.setDescripcion(request.getDescripcion());
        existing.setRegistradoPor(request.getRegistradoPor());
        Anotaciones saved = repository.save(existing);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Anotaciones existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotación no encontrada con id: " + id));
        repository.delete(existing);
    }

    @Override
    public List<AnotacionesDto> findByEstudianteId(Long estudianteId) {
        return repository.findByEstudianteId(estudianteId).stream().map(this::toDto).collect(Collectors.toList());
    }

    private AnotacionesDto toDto(Anotaciones a) {
        AnotacionesDto d = new AnotacionesDto();
        d.setId(a.getId());
        d.setEstudianteId(a.getEstudianteId());
        d.setFecha(a.getFecha());
        d.setTipo(a.getTipo());
        d.setDescripcion(a.getDescripcion());
        d.setRegistradoPor(a.getRegistradoPor());
        return d;
    }
}
