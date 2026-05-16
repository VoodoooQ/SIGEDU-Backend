package com.gestion.educativa.estructura.academica.services;

import com.gestion.educativa.estructura.academica.models.dto.PeriodoDto;
import com.gestion.educativa.estructura.academica.models.request.PeriodoRequest;

import java.util.List;

public interface PeriodoService {

	List<PeriodoDto> findAll();

	PeriodoDto findById(Long id);

	PeriodoDto create(PeriodoRequest request);

	PeriodoDto update(Long id, PeriodoRequest request);

	void delete(Long id);

}
