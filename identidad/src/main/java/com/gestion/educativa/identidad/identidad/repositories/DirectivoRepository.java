package com.gestion.educativa.identidad.identidad.repositories;

import com.gestion.educativa.identidad.identidad.models.entity.Directivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectivoRepository extends JpaRepository<Directivo, String> {
}
