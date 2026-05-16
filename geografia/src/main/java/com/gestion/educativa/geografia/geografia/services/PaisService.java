package com.gestion.educativa.geografia.geografia.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.educativa.geografia.geografia.models.entity.Pais;
import com.gestion.educativa.geografia.geografia.repositories.PaisRepository;
@Service
public class PaisService {
    @Autowired
    private PaisRepository paisRepository;
    public List<Pais> obtenerPaises() {
        return paisRepository.findAll();
    }
}