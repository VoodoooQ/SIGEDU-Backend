package com.gestion.educativa.academica.gestionacademica.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gestion.educativa.academica.gestionacademica.models.Asignatura;
import com.gestion.educativa.academica.gestionacademica.models.request.Agregar_Modificar_Asignatura;
import com.gestion.educativa.academica.gestionacademica.repositories.AsignaturaRepository;
@Service
public class AsignaturaService {
    @Autowired 
    private AsignaturaRepository asignaturaRepository;
    public Asignatura agregarAsignatura(Agregar_Modificar_Asignatura request) {
        Asignatura nueva_asignatura = new Asignatura();
        nueva_asignatura.setNombre_asignatura(request.getNombre_asignatura());
        nueva_asignatura.setId_nivel_ref(request.getId_nivel_ref());
        nueva_asignatura.setRun_docente_ref(request.getRun_docente_ref());
        return asignaturaRepository.save(nueva_asignatura);
    }
    public List<Asignatura> obtenerAsignaturas() {
        return asignaturaRepository.findAll();
    }
    public Asignatura modificarAsignatura(int id_asignatura, Agregar_Modificar_Asignatura request) {
        Asignatura asignaturaExistente = asignaturaRepository.findById(id_asignatura)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada"));
        asignaturaExistente.setNombre_asignatura(request.getNombre_asignatura());
        asignaturaExistente.setId_nivel_ref(request.getId_nivel_ref());
        asignaturaExistente.setRun_docente_ref(request.getRun_docente_ref());
        return asignaturaRepository.save(asignaturaExistente);
    }
    public String eliminarAsignatura(int id_asignatura) {
        if (!asignaturaRepository.existsById(id_asignatura)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Asignatura no encontrada"); 
        }
        asignaturaRepository.deleteById(id_asignatura);
        return "Asignatura eliminada correctamente";
    }
}