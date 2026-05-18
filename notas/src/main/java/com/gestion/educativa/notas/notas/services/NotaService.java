package com.gestion.educativa.notas.notas.services;

import java.util.List;
import com.gestion.educativa.notas.notas.models.entity.Nota;
import com.gestion.educativa.notas.notas.models.request.NotaRequest;
import com.gestion.educativa.notas.notas.repositories.NotaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NotaService {

    private final NotaRepository notaRepository;

    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public List<Nota> listar() {
        return notaRepository.findAll();
    }

    public Nota crear(NotaRequest request) {
        Nota nota = mapearRequestANota(request);
        return notaRepository.save(nota);
    }

    public Nota actualizar(Long idNota, NotaRequest request) {
        Nota notaExistente = notaRepository.findById(idNota)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada"));

        notaExistente.setRunEstudiante(request.getRunEstudiante());
        notaExistente.setCodigoAsignatura(request.getCodigoAsignatura());
        notaExistente.setPeriodo(request.getPeriodo());
        notaExistente.setTipoEvaluacion(request.getTipoEvaluacion());
        notaExistente.setPonderacion(request.getPonderacion());
        notaExistente.setCalificacion(request.getCalificacion());
        notaExistente.setObservaciones(request.getObservaciones());
        return notaRepository.save(notaExistente);
    }

    public void eliminar(Long idNota) {
        if (!notaRepository.existsById(idNota)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada");
        }
        notaRepository.deleteById(idNota);
    }

    private Nota mapearRequestANota(NotaRequest request) {
        Nota nota = new Nota();
        nota.setRunEstudiante(request.getRunEstudiante());
        nota.setCodigoAsignatura(request.getCodigoAsignatura());
        nota.setPeriodo(request.getPeriodo());
        nota.setTipoEvaluacion(request.getTipoEvaluacion());
        nota.setPonderacion(request.getPonderacion());
        nota.setCalificacion(request.getCalificacion());
        nota.setObservaciones(request.getObservaciones());
        return nota;
    }
}
