package com.gestion.educativa.reuniones.reuniones.services;

import java.util.List;
import java.util.Optional;
import com.gestion.educativa.reuniones.reuniones.models.entity.BitacoraReunionApoderado;
import com.gestion.educativa.reuniones.reuniones.models.entity.BitacoraReunionGeneral;
import com.gestion.educativa.reuniones.reuniones.models.entity.BitacoraReunionP1aP1;
import com.gestion.educativa.reuniones.reuniones.repositories.BitacoraReunionApoderadoRepository;
import com.gestion.educativa.reuniones.reuniones.repositories.BitacoraReunionGeneralRepository;
import com.gestion.educativa.reuniones.reuniones.repositories.BitacoraReunionP1aP1Repository;
import org.springframework.stereotype.Service;

@Service
public class ReunionService {

    private final BitacoraReunionGeneralRepository reunionGeneralRepository;
    private final BitacoraReunionApoderadoRepository reunionApoderadoRepository;
    private final BitacoraReunionP1aP1Repository reunionP1aP1Repository;

    public ReunionService(
            BitacoraReunionGeneralRepository reunionGeneralRepository,
            BitacoraReunionApoderadoRepository reunionApoderadoRepository,
            BitacoraReunionP1aP1Repository reunionP1aP1Repository) {
        this.reunionGeneralRepository = reunionGeneralRepository;
        this.reunionApoderadoRepository = reunionApoderadoRepository;
        this.reunionP1aP1Repository = reunionP1aP1Repository;
    }

    public List<BitacoraReunionGeneral> listarGenerales() {
        return reunionGeneralRepository.findAll();
    }

    public BitacoraReunionGeneral guardarGeneral(BitacoraReunionGeneral reunionGeneral) {
        return reunionGeneralRepository.save(reunionGeneral);
    }

    public Optional<BitacoraReunionGeneral> buscarGeneralPorId(Long idBitacoraReunionGeneral) {
        return reunionGeneralRepository.findById(idBitacoraReunionGeneral);
    }

    public void eliminarGeneral(Long idBitacoraReunionGeneral) {
        reunionGeneralRepository.deleteById(idBitacoraReunionGeneral);
    }

    public List<BitacoraReunionApoderado> listarApoderados() {
        return reunionApoderadoRepository.findAll();
    }

    public BitacoraReunionApoderado guardarApoderado(BitacoraReunionApoderado reunionApoderado) {
        return reunionApoderadoRepository.save(reunionApoderado);
    }

    public Optional<BitacoraReunionApoderado> buscarApoderadoPorId(Long idBitacoraReunionApoderado) {
        return reunionApoderadoRepository.findById(idBitacoraReunionApoderado);
    }

    public void eliminarApoderado(Long idBitacoraReunionApoderado) {
        reunionApoderadoRepository.deleteById(idBitacoraReunionApoderado);
    }

    public List<BitacoraReunionP1aP1> listarP1aP1() {
        return reunionP1aP1Repository.findAll();
    }

    public BitacoraReunionP1aP1 guardarP1aP1(BitacoraReunionP1aP1 reunionP1aP1) {
        return reunionP1aP1Repository.save(reunionP1aP1);
    }

    public Optional<BitacoraReunionP1aP1> buscarP1aP1PorId(Long idBitacoraReunionP1aP1) {
        return reunionP1aP1Repository.findById(idBitacoraReunionP1aP1);
    }

    public void eliminarP1aP1(Long idBitacoraReunionP1aP1) {
        reunionP1aP1Repository.deleteById(idBitacoraReunionP1aP1);
    }
}