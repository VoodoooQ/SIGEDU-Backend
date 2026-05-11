package com.gestion.educativa.identidad.identidad.repositories;

import java.util.List;
import com.gestion.educativa.identidad.identidad.models.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocenteRepository extends JpaRepository<Docente, String> {

    List<Docente> findByEspecialidad(String especialidad);
}
