package com.gestion.educativa.geografia.geografia.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.educativa.geografia.geografia.models.Ciudad;
import com.gestion.educativa.geografia.geografia.repositories.CiudadRepository;
@Service
public class CiudadService {
    @Autowired
    private CiudadRepository ciudadRepository;
    public List<Ciudad> obtenerCiudadesPorRegion(int idRegion) {
        return ciudadRepository.findByRegion_IdRegion(idRegion);
    }
    
    
}