package com.gestion.educativa.estructura.academica.repositories;

import com.gestion.educativa.estructura.academica.models.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {

}
