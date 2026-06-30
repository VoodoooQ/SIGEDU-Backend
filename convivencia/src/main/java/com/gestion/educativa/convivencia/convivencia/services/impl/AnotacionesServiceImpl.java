package com.gestion.educativa.convivencia.convivencia.services.impl;

import com.gestion.educativa.convivencia.convivencia.exceptions.ResourceNotFoundException;
import com.gestion.educativa.convivencia.convivencia.models.dto.AnotacionesDto;
import com.gestion.educativa.convivencia.convivencia.models.entity.Anotaciones;
import com.gestion.educativa.convivencia.convivencia.models.request.AnotacionRequest;
import com.gestion.educativa.convivencia.convivencia.repositories.AnotacionesRepository;
import com.gestion.educativa.convivencia.convivencia.services.AnotacionesService;
import com.gestion.educativa.convivencia.convivencia.services.MatriculaClientService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AnotacionesServiceImpl implements AnotacionesService {

    private final AnotacionesRepository repository;
    private final MatriculaClientService matriculaClientService;

    public AnotacionesServiceImpl(AnotacionesRepository repository, MatriculaClientService matriculaClientService) {
        this.repository = repository;
        this.matriculaClientService = matriculaClientService;
    }

    @Override
    public List<AnotacionesDto> findAll() { return repository.findAll().stream().map(this::toDto).collect(Collectors.toList()); }

    @Override
    public AnotacionesDto findById(Long id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Anotacion no encontrada con id: " + id));
    }

    @Override
    public AnotacionesDto create(AnotacionRequest request, String runAutorRef) {
        validarMatriculaActiva(request.getRunEstudianteRef());

        Anotaciones a = new Anotaciones();
        a.setRunEstudianteRef(request.getRunEstudianteRef());
        a.setFecha(request.getFecha());
        a.setTipo(request.getTipo());
        a.setDescripcion(request.getDescripcion());
        a.setRunAutorRef(runAutorRef);
        return toDto(repository.save(a));
    }

    @Override
    public AnotacionesDto update(Long id, AnotacionRequest request) {
        Anotaciones existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotacion no encontrada con id: " + id));
        validarMatriculaActiva(request.getRunEstudianteRef());
        existing.setRunEstudianteRef(request.getRunEstudianteRef());
        existing.setFecha(request.getFecha());
        existing.setTipo(request.getTipo());
        existing.setDescripcion(request.getDescripcion());
        return toDto(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Anotaciones existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotacion no encontrada con id: " + id));
        repository.delete(existing);
    }

    @Override
    public List<AnotacionesDto> findByRunEstudianteRef(String runEstudianteRef) {
        return repository.findByRunEstudianteRef(runEstudianteRef).stream().map(this::toDto).collect(Collectors.toList());
    }

    private void validarMatriculaActiva(String runEstudianteRef) {
        if (!matriculaClientService.estudianteMatriculado(runEstudianteRef)) {
            log.warn("Estudiante {} no registra matricula activa o matricula devolvio lista vacia", runEstudianteRef);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estudiante no registra matricula activa");
        }
    }

    private AnotacionesDto toDto(Anotaciones a) {
        AnotacionesDto d = new AnotacionesDto();
        d.setId(a.getId());
        d.setRunEstudianteRef(a.getRunEstudianteRef());
        d.setFecha(a.getFecha());
        d.setTipo(a.getTipo());
        d.setDescripcion(a.getDescripcion());
        d.setRunAutorRef(a.getRunAutorRef());
        return d;
    }
}