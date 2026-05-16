package com.gestion.educativa.academica.gestionacademica.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.gestion.educativa.academica.gestionacademica.models.entity.Asignatura;
import com.gestion.educativa.academica.gestionacademica.models.entity.BitacoraAsignatura;
import com.gestion.educativa.academica.gestionacademica.models.request.AgregarBitacora;
import com.gestion.educativa.academica.gestionacademica.models.request.ModificarBitacora;
import com.gestion.educativa.academica.gestionacademica.repositories.AsignaturaRepository;
import com.gestion.educativa.academica.gestionacademica.repositories.BitacoraAsignaturaRepository;
@Service
public class BitacoraAsignaturaService {
    @Autowired
    private BitacoraAsignaturaRepository bitacoraAsignaturaRepository;
    @Autowired
    private AsignaturaRepository asignaturaRepository;

    
    public BitacoraAsignatura registrarBitacoraAsignatura(AgregarBitacora request,String run) {
        BitacoraAsignatura bitacora = new BitacoraAsignatura();
        bitacora.setFecha(request.getFecha());
        bitacora.setContenido_visto(request.getContenido_visto());
        bitacora.setObservaciones(request.getObservaciones());

        Asignatura asig = asignaturaRepository.findById(request.getId_asignatura())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Asignatura no encontrada"));
        bitacora.setId_asignatura(asig.getId_asignatura());

        bitacora.setRun_docente_ref(run);

        return bitacoraAsignaturaRepository.save(bitacora);
    }

   
    public String eliminarBitacoraAsignatura(int id_bitacora) {
        if (!bitacoraAsignaturaRepository.existsById(id_bitacora)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Bitácora no encontrada"); 
        }
        bitacoraAsignaturaRepository.deleteById(id_bitacora);
        return "Bitácora eliminada correctamente";
    }

    public BitacoraAsignatura modificarBitacoraAsignatura(int id_bitacora, ModificarBitacora request) {
        BitacoraAsignatura bitacoraExistente = bitacoraAsignaturaRepository.findById(id_bitacora)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bitácora no encontrada"));
        bitacoraExistente.setContenido_visto(request.getContenido_visto());
        bitacoraExistente.setObservaciones(request.getObservaciones());
        Asignatura asig = asignaturaRepository.findById(request.getId_asignatura())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Asignatura no encontrada"));
        bitacoraExistente.setId_asignatura(asig.getId_asignatura());
        return bitacoraAsignaturaRepository.save(bitacoraExistente);
    }
    
}
