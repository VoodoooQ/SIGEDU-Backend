package com.gestion.educativa.geografia.geografia.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.educativa.geografia.geografia.models.entity.Comuna;
import com.gestion.educativa.geografia.geografia.repositories.ComunaRepository;
@Service
public class ComunaService {
    @Autowired 
    private ComunaRepository comunaRepository;
    public List<Comuna> obtenerComunasPorCiudad(int idCiudad) {
        return comunaRepository.findByCiudad_IdCiudad(idCiudad);
    }
    
}