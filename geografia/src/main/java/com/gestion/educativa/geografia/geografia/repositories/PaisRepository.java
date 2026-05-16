package com.gestion.educativa.geografia.geografia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.educativa.geografia.geografia.models.Pais;
@Repository
public interface PaisRepository extends JpaRepository<Pais, Integer> {

    
}