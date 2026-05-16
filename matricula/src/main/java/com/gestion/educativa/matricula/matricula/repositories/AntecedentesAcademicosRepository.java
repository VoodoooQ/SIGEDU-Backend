package com.gestion.educativa.matricula.matricula.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Academicos;
@Repository
public interface AntecedentesAcademicosRepository extends JpaRepository<Antecedentes_Academicos, Integer> {
    @Query("SELECT a FROM Antecedentes_Academicos a WHERE a.run_estudiante_ref = :estudiante")
    List<Antecedentes_Academicos> findByEstudiante(@Param("estudiante") String estudiante);
}
