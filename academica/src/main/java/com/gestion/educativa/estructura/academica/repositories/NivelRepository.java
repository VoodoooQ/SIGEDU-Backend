package com.gestion.educativa.estructura.academica.repositories;

import com.gestion.educativa.estructura.academica.models.entity.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NivelRepository extends JpaRepository<Nivel, Long> {

}
