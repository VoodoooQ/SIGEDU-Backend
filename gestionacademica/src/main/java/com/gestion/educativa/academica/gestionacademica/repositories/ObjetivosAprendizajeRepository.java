package com.gestion.educativa.academica.gestionacademica.repositories;

import java.util.List;

import com.gestion.educativa.academica.gestionacademica.models.entity.ObjetivosAprendizaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjetivosAprendizajeRepository extends JpaRepository<ObjetivosAprendizaje, Integer> {
    @Query("SELECT o FROM ObjetivosAprendizaje o WHERE o.asignatura.id_asignatura = :idAsignatura")
    List<ObjetivosAprendizaje> findByAsignaturaId(@Param("idAsignatura") int idAsignatura);
}
