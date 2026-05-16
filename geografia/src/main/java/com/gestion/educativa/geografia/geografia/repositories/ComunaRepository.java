package com.gestion.educativa.geografia.geografia.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.educativa.geografia.geografia.models.entity.Comuna;
@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Integer> {    
    List<Comuna> findByCiudad_IdCiudad(int id_ciudad);

}