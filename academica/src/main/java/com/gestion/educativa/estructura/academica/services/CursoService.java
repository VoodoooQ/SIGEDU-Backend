package com.gestion.educativa.estructura.academica.services;

import com.gestion.educativa.estructura.academica.models.dto.CursoDto;
import com.gestion.educativa.estructura.academica.models.request.CursoRequest;

import java.util.List;

public interface CursoService {

	List<CursoDto> findAll();

	CursoDto findById(Long id);

	CursoDto create(CursoRequest request);

	CursoDto update(Long id, CursoRequest request);

	void delete(Long id);

}

