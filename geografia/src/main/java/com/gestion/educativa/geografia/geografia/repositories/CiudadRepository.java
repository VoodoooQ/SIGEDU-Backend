package com.gestion.educativa.geografia.geografia.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.educativa.geografia.geografia.models.entity.Ciudad;
@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {
    List<Ciudad> findByRegion_IdRegion(int idRegion);
    
}