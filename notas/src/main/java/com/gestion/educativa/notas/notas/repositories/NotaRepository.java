package com.gestion.educativa.notas.notas.repositories;

import com.gestion.educativa.notas.notas.models.entity.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota, Long> {
}