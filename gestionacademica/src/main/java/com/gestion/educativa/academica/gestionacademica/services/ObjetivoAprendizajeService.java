package com.gestion.educativa.academica.gestionacademica.services;

import java.util.List;

import com.gestion.educativa.academica.gestionacademica.models.entity.Asignatura;
import com.gestion.educativa.academica.gestionacademica.models.entity.ObjetivosAprendizaje;
import com.gestion.educativa.academica.gestionacademica.models.request.AgregarObjetivo;
import com.gestion.educativa.academica.gestionacademica.repositories.AsignaturaRepository;
import com.gestion.educativa.academica.gestionacademica.repositories.ObjetivosAprendizajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ObjetivoAprendizajeService {
    @Autowired
    private ObjetivosAprendizajeRepository aprendizajeRepository;
    @Autowired
    private AsignaturaRepository asignaturaRepository;

    public ObjetivosAprendizaje agregarObjetivosAprendizaje(AgregarObjetivo request) {
        ObjetivosAprendizaje nuevoObjetivo = new ObjetivosAprendizaje();
        nuevoObjetivo.setCodigo(request.getCodigo());
        nuevoObjetivo.setDescripcion(request.getDescripcion());
        Asignatura asignatura = asignaturaRepository.findById(request.getId_asignatura())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura con ID " + request.getId_asignatura() + " no encontrada"));
        nuevoObjetivo.setAsignatura(asignatura);
        return aprendizajeRepository.save(nuevoObjetivo);
    }

    public List<ObjetivosAprendizaje> obtenerObjetivosPorAsignatura(int id_asignatura) {
        return aprendizajeRepository.findByAsignaturaId(id_asignatura);
    }

    public ObjetivosAprendizaje modificarObjetivo(int id_objetivo, AgregarObjetivo request) {
        ObjetivosAprendizaje objetivoExistente = aprendizajeRepository.findById(id_objetivo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objetivo no encontrado"));
        Asignatura asignatura = asignaturaRepository.findById(request.getId_asignatura())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura con ID " + request.getId_asignatura() + " no encontrada"));
        objetivoExistente.setCodigo(request.getCodigo());
        objetivoExistente.setDescripcion(request.getDescripcion());
        objetivoExistente.setAsignatura(asignatura);
        return aprendizajeRepository.save(objetivoExistente);
    }

    public String eliminarObjetivo(int id_objetivo) {
        if (!aprendizajeRepository.existsById(id_objetivo)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Objetivo no encontrado");
        }
        aprendizajeRepository.deleteById(id_objetivo);
        return "Objetivo eliminado correctamente";
    }
}
