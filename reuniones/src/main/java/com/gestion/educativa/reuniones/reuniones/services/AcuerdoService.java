package com.gestion.educativa.reuniones.reuniones.services;

import java.util.List;
import java.util.Optional;
import com.gestion.educativa.reuniones.reuniones.models.entity.Acuerdo;
import com.gestion.educativa.reuniones.reuniones.repositories.AcuerdoRepository;
import org.springframework.stereotype.Service;

@Service
public class AcuerdoService {

    private final AcuerdoRepository acuerdoRepository;

    public AcuerdoService(AcuerdoRepository acuerdoRepository) {
        this.acuerdoRepository = acuerdoRepository;
    }

    public List<Acuerdo> listar() {
        return acuerdoRepository.findAll();
    }

    public Acuerdo guardar(Acuerdo acuerdo) {
        return acuerdoRepository.save(acuerdo);
    }

    public Optional<Acuerdo> buscarPorId(Long idAcuerdo) {
        return acuerdoRepository.findById(idAcuerdo);
    }

    public void eliminar(Long idAcuerdo) {
        acuerdoRepository.deleteById(idAcuerdo);
    }
}