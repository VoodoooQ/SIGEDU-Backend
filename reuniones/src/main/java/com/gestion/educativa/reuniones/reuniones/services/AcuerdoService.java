package com.gestion.educativa.reuniones.reuniones.services;

import java.util.List;
import com.gestion.educativa.reuniones.reuniones.models.entity.Acuerdo;
import com.gestion.educativa.reuniones.reuniones.models.request.AcuerdoRequest;
import com.gestion.educativa.reuniones.reuniones.repositories.AcuerdoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AcuerdoService {

    private final AcuerdoRepository acuerdoRepository;

    public AcuerdoService(AcuerdoRepository acuerdoRepository) {
        this.acuerdoRepository = acuerdoRepository;
    }

    public List<Acuerdo> listar() {
        return acuerdoRepository.findAll();
    }

    public Acuerdo crear(AcuerdoRequest request) {
        Acuerdo acuerdo = mapearRequestAAcuerdo(request);
        return acuerdoRepository.save(acuerdo);
    }

    public Acuerdo actualizar(Long idAcuerdo, AcuerdoRequest request) {
        Acuerdo acuerdoExistente = acuerdoRepository.findById(idAcuerdo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Acuerdo no encontrado"));

        acuerdoExistente.setIdBitacoraReunionGeneral(request.getIdBitacoraReunionGeneral());
        acuerdoExistente.setDetalleAcuerdo(request.getDetalleAcuerdo());
        acuerdoExistente.setResponsable(request.getResponsable());
        acuerdoExistente.setFechaCompromiso(request.getFechaCompromiso());
        acuerdoExistente.setEstado(request.getEstado());
        acuerdoExistente.setObservaciones(request.getObservaciones());
        return acuerdoRepository.save(acuerdoExistente);
    }

    public void eliminar(Long idAcuerdo) {
        if (!acuerdoRepository.existsById(idAcuerdo)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Acuerdo no encontrado");
        }
        acuerdoRepository.deleteById(idAcuerdo);
    }

    private Acuerdo mapearRequestAAcuerdo(AcuerdoRequest request) {
        Acuerdo acuerdo = new Acuerdo();
        acuerdo.setIdBitacoraReunionGeneral(request.getIdBitacoraReunionGeneral());
        acuerdo.setDetalleAcuerdo(request.getDetalleAcuerdo());
        acuerdo.setResponsable(request.getResponsable());
        acuerdo.setFechaCompromiso(request.getFechaCompromiso());
        acuerdo.setEstado(request.getEstado());
        acuerdo.setObservaciones(request.getObservaciones());
        return acuerdo;
    }
}
