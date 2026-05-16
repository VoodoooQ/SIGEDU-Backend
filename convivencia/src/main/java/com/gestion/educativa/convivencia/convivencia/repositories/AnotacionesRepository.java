package com.gestion.educativa.convivencia.convivencia.repositories;

import com.gestion.educativa.convivencia.convivencia.models.entity.Anotaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnotacionesRepository extends JpaRepository<Anotaciones, Long> {
	
	List<Anotaciones> findByEstudianteId(Long estudianteId);

}
