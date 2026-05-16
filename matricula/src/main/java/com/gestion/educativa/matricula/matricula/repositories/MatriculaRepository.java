package com.gestion.educativa.matricula.matricula.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gestion.educativa.matricula.matricula.models.entity.Matricula;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {
    @Query("SELECT a FROM Matricula a WHERE a.run_estudiante_ref = :estudiante")
    List<Matricula> findByEstudiante(@Param("estudiante") String estudiante);
}
