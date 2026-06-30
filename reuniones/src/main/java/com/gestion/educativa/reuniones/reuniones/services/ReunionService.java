package com.gestion.educativa.reuniones.reuniones.services;

import java.util.List;
import java.util.Optional;
import com.gestion.educativa.reuniones.reuniones.models.entity.BitacoraReunionApoderado;
import com.gestion.educativa.reuniones.reuniones.models.entity.BitacoraReunionGeneral;
import com.gestion.educativa.reuniones.reuniones.models.entity.BitacoraReunionP1aP1;
import com.gestion.educativa.reuniones.reuniones.repositories.BitacoraReunionApoderadoRepository;
import com.gestion.educativa.reuniones.reuniones.repositories.BitacoraReunionGeneralRepository;
import com.gestion.educativa.reuniones.reuniones.repositories.BitacoraReunionP1aP1Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class ReunionService {

    private final BitacoraReunionGeneralRepository reunionGeneralRepository;
    private final BitacoraReunionApoderadoRepository reunionApoderadoRepository;
    private final BitacoraReunionP1aP1Repository reunionP1aP1Repository;
    private final MatriculaClientService matriculaClientService;

    public ReunionService(
            BitacoraReunionGeneralRepository reunionGeneralRepository,
            BitacoraReunionApoderadoRepository reunionApoderadoRepository,
            BitacoraReunionP1aP1Repository reunionP1aP1Repository,
            MatriculaClientService matriculaClientService) {
        this.reunionGeneralRepository = reunionGeneralRepository;
        this.reunionApoderadoRepository = reunionApoderadoRepository;
        this.reunionP1aP1Repository = reunionP1aP1Repository;
        this.matriculaClientService = matriculaClientService;
    }

    public List<BitacoraReunionGeneral> listarGenerales() {
        return reunionGeneralRepository.findAll();
    }

    public BitacoraReunionGeneral guardarGeneral(BitacoraReunionGeneral reunionGeneral) {
        return reunionGeneralRepository.save(reunionGeneral);
    }

    public BitacoraReunionGeneral actualizarGeneral(Long idBitacoraReunionGeneral, BitacoraReunionGeneral reunionGeneral) {
        BitacoraReunionGeneral existente = reunionGeneralRepository.findById(idBitacoraReunionGeneral)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reunion general no encontrada"));
        existente.setFechaReunion(reunionGeneral.getFechaReunion());
        existente.setHoraReunion(reunionGeneral.getHoraReunion());
        existente.setLugar(reunionGeneral.getLugar());
        existente.setTema(reunionGeneral.getTema());
        existente.setObservaciones(reunionGeneral.getObservaciones());
        return reunionGeneralRepository.save(existente);
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

    public BitacoraReunionApoderado actualizarApoderado(Long idBitacoraReunionApoderado, BitacoraReunionApoderado reunionApoderado) {
        BitacoraReunionApoderado existente = reunionApoderadoRepository.findById(idBitacoraReunionApoderado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reunion de apoderado no encontrada"));
        existente.setFechaReunion(reunionApoderado.getFechaReunion());
        existente.setHoraReunion(reunionApoderado.getHoraReunion());
        existente.setRunApoderado(reunionApoderado.getRunApoderado());
        existente.setLugar(reunionApoderado.getLugar());
        existente.setTema(reunionApoderado.getTema());
        existente.setObservaciones(reunionApoderado.getObservaciones());
        return reunionApoderadoRepository.save(existente);
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
        validarMatriculaActiva(reunionP1aP1.getRunEstudiante());
        return reunionP1aP1Repository.save(reunionP1aP1);
    }

    public BitacoraReunionP1aP1 actualizarP1aP1(Long idBitacoraReunionP1aP1, BitacoraReunionP1aP1 reunionP1aP1) {
        BitacoraReunionP1aP1 existente = reunionP1aP1Repository.findById(idBitacoraReunionP1aP1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reunion p1a1 no encontrada"));
        validarMatriculaActiva(reunionP1aP1.getRunEstudiante());
        existente.setFechaReunion(reunionP1aP1.getFechaReunion());
        existente.setHoraReunion(reunionP1aP1.getHoraReunion());
        existente.setRunEstudiante(reunionP1aP1.getRunEstudiante());
        existente.setLugar(reunionP1aP1.getLugar());
        existente.setTema(reunionP1aP1.getTema());
        existente.setObservaciones(reunionP1aP1.getObservaciones());
        return reunionP1aP1Repository.save(existente);
    }

    public Optional<BitacoraReunionP1aP1> buscarP1aP1PorId(Long idBitacoraReunionP1aP1) {
        return reunionP1aP1Repository.findById(idBitacoraReunionP1aP1);
    }

    public void eliminarP1aP1(Long idBitacoraReunionP1aP1) {
        reunionP1aP1Repository.deleteById(idBitacoraReunionP1aP1);
    }

    private void validarMatriculaActiva(String runEstudiante) {
        if (!matriculaClientService.estudianteMatriculado(runEstudiante)) {
            log.warn("No se encontro matricula activa o lista vacia para estudiante {}", runEstudiante);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estudiante no registra matricula activa");
        }
    }
}