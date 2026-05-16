package com.gestion.educativa.geografia.geografia.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gestion.educativa.geografia.geografia.models.Direccion;
@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
    @Query("SELECT d FROM Direccion d WHERE d.run_usuario_ref = :runUsuarioRef")
    List<Direccion> findByRunUsuarioRef(@Param("runUsuarioRef") String runUsuarioRef);
    
}