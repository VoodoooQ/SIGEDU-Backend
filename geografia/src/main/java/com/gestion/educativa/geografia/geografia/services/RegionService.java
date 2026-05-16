package com.gestion.educativa.geografia.geografia.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.educativa.geografia.geografia.models.Region;
import com.gestion.educativa.geografia.geografia.repositories.RegionRepository;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;
    public List<Region> obtenerRegionporPais(int idPais) {
       return regionRepository.findByPais_IdPais(idPais);
    }
    
}