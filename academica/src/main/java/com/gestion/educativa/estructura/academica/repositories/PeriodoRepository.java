package com.gestion.educativa.estructura.academica.repositories;

import com.gestion.educativa.estructura.academica.models.entity.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodoRepository extends JpaRepository<Periodo, Long> {

}
