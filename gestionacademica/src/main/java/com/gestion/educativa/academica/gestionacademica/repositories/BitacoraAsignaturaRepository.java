package com.gestion.educativa.academica.gestionacademica.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gestion.educativa.academica.gestionacademica.models.BitacoraAsignatura;
@Repository
public interface BitacoraAsignaturaRepository extends JpaRepository<BitacoraAsignatura, Integer>{
}
