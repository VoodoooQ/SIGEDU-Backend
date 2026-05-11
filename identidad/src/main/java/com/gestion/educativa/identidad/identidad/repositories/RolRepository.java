package com.gestion.educativa.identidad.identidad.repositories;

import java.util.Optional;
import com.gestion.educativa.identidad.identidad.models.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    Optional<Rol> findByNombreRol(String nombreRol);
}
