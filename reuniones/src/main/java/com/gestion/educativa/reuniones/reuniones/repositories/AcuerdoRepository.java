package com.gestion.educativa.reuniones.reuniones.repositories;

import com.gestion.educativa.reuniones.reuniones.models.entity.Acuerdo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcuerdoRepository extends JpaRepository<Acuerdo, Long> {
}