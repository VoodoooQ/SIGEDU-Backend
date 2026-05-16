package com.gestion.educativa.matricula.matricula.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.educativa.matricula.matricula.models.Antecedentes_Apoderado;
@Repository
public interface AntecedentesApoderadoRepository extends JpaRepository<Antecedentes_Apoderado, Integer> {


}
