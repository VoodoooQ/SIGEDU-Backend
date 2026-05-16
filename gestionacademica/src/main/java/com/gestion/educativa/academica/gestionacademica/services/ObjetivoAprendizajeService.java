package com.gestion.educativa.academica.gestionacademica.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gestion.educativa.academica.gestionacademica.models.Asignatura;
import com.gestion.educativa.academica.gestionacademica.models.ObjetivosAprendizaje;
import com.gestion.educativa.academica.gestionacademica.models.request.AgregarObjetivo;
import com.gestion.educativa.academica.gestionacademica.repositories.AsignaturaRepository;
import com.gestion.educativa.academica.gestionacademica.repositories.ObjetivosAprendizajeRepository;

@Service
public class ObjetivoAprendizajeService {
    @Autowired
    private ObjetivosAprendizajeRepository aprendizajeRepository;
    @Autowired
    private AsignaturaRepository asignaturaRepository;
    public ObjetivosAprendizaje agregarObjetivosAprendizaje(AgregarObjetivo request){
        ObjetivosAprendizaje nuObjetivosAprendizaje= new ObjetivosAprendizaje();
        nuObjetivosAprendizaje.setCodigo(request.getCodigo());
        nuObjetivosAprendizaje.setDescripcion(request.getDescripcion());
        Asignatura asig = asignaturaRepository.findById(request.getId_asignatura()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Asignatura con ID " + request.getId_asignatura() + " no encontrada"));
        nuObjetivosAprendizaje.setAsignatura(asig);
        return aprendizajeRepository.save(nuObjetivosAprendizaje);
    }
    
}