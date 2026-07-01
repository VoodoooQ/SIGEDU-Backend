package com.gestion.educativa.convivencia.convivencia.services;

import com.gestion.educativa.convivencia.convivencia.models.dto.HojaVidaEstudianteDto;
import com.gestion.educativa.convivencia.convivencia.models.request.HojaVidaRequest;

import java.util.List;

public interface HojaVidaService {
    List<HojaVidaEstudianteDto> findAll();
    HojaVidaEstudianteDto findById(Long id);
    HojaVidaEstudianteDto create(HojaVidaRequest request, String runAutorRef);
    HojaVidaEstudianteDto update(Long id, HojaVidaRequest request);
    void delete(Long id);
    List<HojaVidaEstudianteDto> findByRunEstudianteRef(String runEstudianteRef);
}