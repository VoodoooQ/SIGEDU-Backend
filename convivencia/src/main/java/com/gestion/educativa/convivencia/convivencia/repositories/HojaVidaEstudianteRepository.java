package com.gestion.educativa.convivencia.convivencia.repositories;

import com.gestion.educativa.convivencia.convivencia.models.entity.HojaVidaEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HojaVidaEstudianteRepository extends JpaRepository<HojaVidaEstudiante, Long> {
	
	List<HojaVidaEstudiante> findByEstudianteId(Long estudianteId);

}
