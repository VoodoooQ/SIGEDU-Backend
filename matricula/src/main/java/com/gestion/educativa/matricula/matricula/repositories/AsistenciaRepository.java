package com.gestion.educativa.matricula.matricula.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.gestion.educativa.matricula.matricula.models.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByRunEstudianteRefOrderByFechaDesc(String runEstudianteRef);
    Optional<Asistencia> findByRunEstudianteRefAndFecha(String runEstudianteRef, LocalDate fecha);
}
