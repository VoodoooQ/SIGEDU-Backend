package com.gestion.educativa.identidad.identidad.repositories;

import com.gestion.educativa.identidad.identidad.models.entity.Inspector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectorRepository extends JpaRepository<Inspector, String> {
}
