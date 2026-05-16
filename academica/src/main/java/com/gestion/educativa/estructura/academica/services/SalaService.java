package com.gestion.educativa.estructura.academica.services;

import com.gestion.educativa.estructura.academica.models.dto.SalaDto;
import com.gestion.educativa.estructura.academica.models.request.SalaRequest;

import java.util.List;

public interface SalaService {

	List<SalaDto> findAll();

	SalaDto findById(Long id);

	SalaDto create(SalaRequest request);

	SalaDto update(Long id, SalaRequest request);

	void delete(Long id);

}
