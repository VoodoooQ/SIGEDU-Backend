package com.gestion.educativa.notas.notas.repositories;

import com.gestion.educativa.notas.notas.models.entity.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByRunEstudiante(String runEstudiante);
}