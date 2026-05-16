package com.gestion.educativa.estructura.academica.services;

import com.gestion.educativa.estructura.academica.models.dto.NivelDto;
import com.gestion.educativa.estructura.academica.models.request.NivelRequest;

import java.util.List;

public interface NivelService {

	List<NivelDto> findAll();

	NivelDto findById(Long id);

	NivelDto create(NivelRequest request);

	NivelDto update(Long id, NivelRequest request);

	void delete(Long id);

}
