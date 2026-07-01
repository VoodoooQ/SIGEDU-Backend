package com.gestion.educativa.academica.gestionacademica.repositories;

import java.util.List;

import com.gestion.educativa.academica.gestionacademica.models.entity.BitacoraAsignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BitacoraAsignaturaRepository extends JpaRepository<BitacoraAsignatura, Integer> {
    @Query("SELECT b FROM BitacoraAsignatura b WHERE b.id_asignatura = :idAsignatura")
    List<BitacoraAsignatura> findByAsignaturaId(@Param("idAsignatura") int idAsignatura);
}
