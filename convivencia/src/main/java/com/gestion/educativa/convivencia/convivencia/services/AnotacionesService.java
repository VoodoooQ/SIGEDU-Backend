package com.gestion.educativa.convivencia.convivencia.services;

import com.gestion.educativa.convivencia.convivencia.models.dto.AnotacionesDto;
import com.gestion.educativa.convivencia.convivencia.models.request.AnotacionRequest;

import java.util.List;

public interface AnotacionesService {
    List<AnotacionesDto> findAll();
    AnotacionesDto findById(Long id);
    AnotacionesDto create(AnotacionRequest request, String runAutorRef);
    AnotacionesDto update(Long id, AnotacionRequest request);
    void delete(Long id);
    List<AnotacionesDto> findByRunEstudianteRef(String runEstudianteRef);
}