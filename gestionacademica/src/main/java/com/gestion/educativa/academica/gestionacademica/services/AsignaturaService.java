package com.gestion.educativa.academica.gestionacademica.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import com.gestion.educativa.academica.gestionacademica.models.dto.NivelDTO;
import com.gestion.educativa.academica.gestionacademica.models.entity.Asignatura;
import com.gestion.educativa.academica.gestionacademica.models.request.Agregar_Modificar_Asignatura;
import com.gestion.educativa.academica.gestionacademica.repositories.AsignaturaRepository;

@Service
public class AsignaturaService {
    @Autowired 
    private AsignaturaRepository asignaturaRepository;
    @Autowired
    private WebClient academicaWebClient;
    @Autowired
    private ValidarDocenteService validarDocenteService;


    public Asignatura agregarAsignatura(Agregar_Modificar_Asignatura request) {
        Asignatura nueva_asignatura = new Asignatura();
        nueva_asignatura.setNombre_asignatura(request.getNombre_asignatura());
        NivelDTO nivel=null;
        try{
            nivel = academicaWebClient.get()
                .uri("/api/academica/niveles/"+request.getId_nivel_ref())
                .retrieve()
                .bodyToMono(NivelDTO.class)
                .block();
        }  catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nivel no encontrado");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al conectar con el microservicio de Estructura Académica");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado de comunicación");
        }
        nueva_asignatura.setId_nivel_ref(request.getId_nivel_ref());
        
        if (!validarDocenteService.validarDocente(request.getRun_docente_ref())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Docente no válido");
        }
        nueva_asignatura.setRun_docente_ref(request.getRun_docente_ref());
        return asignaturaRepository.save(nueva_asignatura);
    }


    public List<Asignatura> obtenerAsignaturas() {
        return asignaturaRepository.findAll();
    }


    public Asignatura modificarAsignatura(int id_asignatura, Agregar_Modificar_Asignatura request) {
        Asignatura asignaturaExistente = asignaturaRepository.findById(id_asignatura).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada"));
        asignaturaExistente.setNombre_asignatura(request.getNombre_asignatura());

        NivelDTO nivel=null;
        try{
            nivel = academicaWebClient.get()
                .uri("/api/academica/niveles/"+request.getId_nivel_ref())
                .retrieve()
                .bodyToMono(NivelDTO.class)
                .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nivel no encontrado");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al conectar con el microservicio de Estructura Académica");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado de comunicación");
        }
        asignaturaExistente.setId_nivel_ref(request.getId_nivel_ref());

        if (!validarDocenteService.validarDocente(request.getRun_docente_ref())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Docente no válido");
        }
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