package com.gestion.educativa.academica.gestionacademica.services;

import java.util.List;

import com.gestion.educativa.academica.gestionacademica.models.entity.Asignatura;
import com.gestion.educativa.academica.gestionacademica.models.entity.BitacoraAsignatura;
import com.gestion.educativa.academica.gestionacademica.models.request.AgregarBitacora;
import com.gestion.educativa.academica.gestionacademica.models.request.ModificarBitacora;
import com.gestion.educativa.academica.gestionacademica.repositories.AsignaturaRepository;
import com.gestion.educativa.academica.gestionacademica.repositories.BitacoraAsignaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BitacoraAsignaturaService {
    @Autowired
    private BitacoraAsignaturaRepository bitacoraAsignaturaRepository;
    @Autowired
    private AsignaturaRepository asignaturaRepository;

    public BitacoraAsignatura registrarBitacoraAsignatura(AgregarBitacora request, String run) {
        BitacoraAsignatura bitacora = new BitacoraAsignatura();
        bitacora.setFecha(request.getFecha());
        bitacora.setContenido_visto(request.getContenido_visto());
        bitacora.setObservaciones(request.getObservaciones());

        Asignatura asignatura = asignaturaRepository.findById(request.getId_asignatura())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada"));
        bitacora.setId_asignatura(asignatura.getId_asignatura());

        String runDocente = (run != null && !run.isBlank()) ? run : asignatura.getRun_docente_ref();
        if (runDocente == null || runDocente.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No fue posible determinar el docente de la bitacora");
        }
        bitacora.setRun_docente_ref(runDocente);

        return bitacoraAsignaturaRepository.save(bitacora);
    }

    public List<BitacoraAsignatura> obtenerBitacorasPorAsignatura(int id_asignatura) {
        return bitacoraAsignaturaRepository.findByAsignaturaId(id_asignatura);
    }

    public BitacoraAsignatura obtenerBitacoraPorId(int id_bitacora) {
        return bitacoraAsignaturaRepository.findById(id_bitacora)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bitacora no encontrada"));
    }

    public String eliminarBitacoraAsignatura(int id_bitacora) {
        if (!bitacoraAsignaturaRepository.existsById(id_bitacora)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bitacora no encontrada");
        }
        bitacoraAsignaturaRepository.deleteById(id_bitacora);
        return "Bitacora eliminada correctamente";
    }

    public BitacoraAsignatura modificarBitacoraAsignatura(int id_bitacora, ModificarBitacora request) {
        BitacoraAsignatura bitacoraExistente = obtenerBitacoraPorId(id_bitacora);
        bitacoraExistente.setContenido_visto(request.getContenido_visto());
        bitacoraExistente.setObservaciones(request.getObservaciones());
        Asignatura asignatura = asignaturaRepository.findById(request.getId_asignatura())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada"));
        bitacoraExistente.setId_asignatura(asignatura.getId_asignatura());
        return bitacoraAsignaturaRepository.save(bitacoraExistente);
    }
}
