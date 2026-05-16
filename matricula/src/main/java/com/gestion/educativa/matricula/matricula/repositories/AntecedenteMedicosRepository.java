package com.gestion.educativa.matricula.matricula.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Medicos;
@Repository
public interface AntecedenteMedicosRepository extends JpaRepository<Antecedentes_Medicos, Integer> {
    @Query("SELECT a FROM Antecedentes_Medicos a WHERE a.run_estudiante_ref = :estudiante")
    List<Antecedentes_Medicos> findByEstudiante(@Param("estudiante") String estudiante);
}
