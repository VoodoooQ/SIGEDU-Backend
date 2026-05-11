package com.gestion.educativa.identidad.identidad.repositories;

import java.util.List;
import com.gestion.educativa.identidad.identidad.models.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, String> {

    List<Estudiante> findByApoderado_RunUsuario(String runApoderado);
}
