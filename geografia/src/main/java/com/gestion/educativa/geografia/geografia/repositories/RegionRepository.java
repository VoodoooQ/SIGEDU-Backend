package com.gestion.educativa.geografia.geografia.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.educativa.geografia.geografia.models.Region;
@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {
   List<Region> findByPais_IdPais(int id_pais);
    
}