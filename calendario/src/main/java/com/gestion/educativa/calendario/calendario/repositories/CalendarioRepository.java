package com.gestion.educativa.calendario.calendario.repositories;

import com.gestion.educativa.calendario.calendario.models.entity.EventoCalendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarioRepository extends JpaRepository<EventoCalendario, Long> {

	List<EventoCalendario> findByFechaInicio(LocalDate fecha);

	List<EventoCalendario> findByTipo(String tipo);

}
