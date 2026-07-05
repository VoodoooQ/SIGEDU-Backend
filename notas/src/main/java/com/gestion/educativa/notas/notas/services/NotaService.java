package com.gestion.educativa.notas.notas.services;

import java.time.LocalDate;
import java.util.List;
import com.gestion.educativa.notas.notas.models.entity.Nota;
import com.gestion.educativa.notas.notas.models.request.NotaRequest;
import com.gestion.educativa.notas.notas.repositories.NotaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class NotaService {

    private final NotaRepository notaRepository;
    private final GestionAcademicaClientService gestionAcademicaClientService;
    private final MatriculaClientService matriculaClientService;

    public NotaService(
            NotaRepository notaRepository,
            GestionAcademicaClientService gestionAcademicaClientService,
            MatriculaClientService matriculaClientService) {
        this.notaRepository = notaRepository;
        this.gestionAcademicaClientService = gestionAcademicaClientService;
        this.matriculaClientService = matriculaClientService;
    }

    public List<Nota> listar() {
        return notaRepository.findAll();
    }

    public List<Nota> listarPorEstudiante(String runEstudiante) {
        return notaRepository.findByRunEstudiante(runEstudiante);
    }

    public Nota crear(NotaRequest request, String runDocenteRef) {
        validarDependencias(request);

        Nota nota = mapearRequestANota(request);
        nota.setRunDocenteRef(runDocenteRef);
        return notaRepository.save(nota);
    }

    public Nota actualizar(Long idNota, NotaRequest request) {
        Nota notaExistente = notaRepository.findById(idNota)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada"));

        validarDependencias(request);

        notaExistente.setRunEstudiante(request.getRunEstudiante());
        notaExistente.setCodigoAsignatura(request.getCodigoAsignatura());
        notaExistente.setPeriodo(request.getPeriodo());
        if (request.getFechaEvaluacion() != null) {
            notaExistente.setFechaEvaluacion(request.getFechaEvaluacion());
        }
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

    public Nota obtenerPorId(Long idNota) {
        return notaRepository.findById(idNota)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada"));
    }

    private void validarDependencias(NotaRequest request) {
        if (request.getCodigoAsignatura() != null
                && gestionAcademicaClientService.obtenerAsignatura(request.getCodigoAsignatura()) == null) {
            log.warn("Asignatura {} no encontrada o gestionacademica no disponible", request.getCodigoAsignatura());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Asignatura no encontrada en gestionacademica");
        }

        if (request.getRunEstudiante() != null
                && !matriculaClientService.estudianteMatriculado(request.getRunEstudiante())) {
            log.warn("Estudiante {} no registra matricula activa o matricula devolvio lista vacia", request.getRunEstudiante());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estudiante no registra matricula activa");
        }
    }

    private Nota mapearRequestANota(NotaRequest request) {
        Nota nota = new Nota();
        nota.setRunEstudiante(request.getRunEstudiante());
        nota.setCodigoAsignatura(request.getCodigoAsignatura());
        nota.setPeriodo(request.getPeriodo());
        nota.setFechaEvaluacion(request.getFechaEvaluacion() != null ? request.getFechaEvaluacion() : LocalDate.now());
        nota.setTipoEvaluacion(request.getTipoEvaluacion());
        nota.setPonderacion(request.getPonderacion());
        nota.setCalificacion(request.getCalificacion());
        nota.setObservaciones(request.getObservaciones());
        return nota;
    }
}