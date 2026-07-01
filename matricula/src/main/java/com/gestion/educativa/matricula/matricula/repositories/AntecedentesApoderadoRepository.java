package com.gestion.educativa.matricula.matricula.repositories;

import java.util.List;

import com.gestion.educativa.matricula.matricula.models.entity.Antecedentes_Apoderado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AntecedentesApoderadoRepository extends JpaRepository<Antecedentes_Apoderado, Integer> {
    @Query("SELECT a FROM Antecedentes_Apoderado a WHERE a.run_apoderado_ref = :apoderado")
    List<Antecedentes_Apoderado> findByApoderado(@Param("apoderado") String apoderado);
}