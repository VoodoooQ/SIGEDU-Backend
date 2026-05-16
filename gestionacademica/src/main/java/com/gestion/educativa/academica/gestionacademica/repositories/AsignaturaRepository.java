package com.gestion.educativa.academica.gestionacademica.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gestion.educativa.academica.gestionacademica.models.Asignatura;
@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Integer>{
}
