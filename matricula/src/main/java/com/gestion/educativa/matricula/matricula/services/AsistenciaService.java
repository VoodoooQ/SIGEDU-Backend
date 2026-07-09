package com.gestion.educativa.matricula.matricula.services;

import java.util.List;
import com.gestion.educativa.matricula.matricula.models.dto.AsistenciaResumenDto;
import com.gestion.educativa.matricula.matricula.models.entity.Asistencia;
import com.gestion.educativa.matricula.matricula.models.request.AsistenciaRequest;
import com.gestion.educativa.matricula.matricula.repositories.AsistenciaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AsistenciaService {
    private final AsistenciaRepository asistenciaRepository;
    private final MatriculaService matriculaService;

    public AsistenciaService(AsistenciaRepository asistenciaRepository, MatriculaService matriculaService) {
        this.asistenciaRepository = asistenciaRepository;
        this.matriculaService = matriculaService;
    }

    public List<Asistencia> listarPorEstudiante(String runEstudiante) {
        return asistenciaRepository.findByRunEstudianteRefOrderByFechaDesc(runEstudiante);
    }

    public AsistenciaResumenDto resumenPorEstudiante(String runEstudiante) {
        List<Asistencia> registros = listarPorEstudiante(runEstudiante);
        long total = registros.size();
        long presentes = registros.stream().filter(item -> "presente".equalsIgnoreCase(item.getEstado())).count();
        long ausentes = registros.stream().filter(item -> "ausente".equalsIgnoreCase(item.getEstado())).count();
        long atrasos = registros.stream().filter(item -> "atrasado".equalsIgnoreCase(item.getEstado())).count();
        int porcentaje = total == 0 ? 0 : Math.round((presentes * 100.0f) / total);
        return new AsistenciaResumenDto(total, presentes, ausentes, atrasos, porcentaje);
    }

    public Asistencia registrar(AsistenciaRequest request, String runDocenteToken) {
        if (matriculaService.obtenerMatriculaPorRun(request.getRunEstudianteRef()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estudiante no registra matricula activa");
        }

        Asistencia asistencia = asistenciaRepository
                .findByRunEstudianteRefAndFecha(request.getRunEstudianteRef(), request.getFecha())
                .orElseGet(Asistencia::new);
        asistencia.setRunEstudianteRef(request.getRunEstudianteRef());
        asistencia.setFecha(request.getFecha());
        asistencia.setEstado(request.getEstado());
        asistencia.setRunDocenteRef(runDocenteToken != null ? runDocenteToken : request.getRunDocenteRef());
        asistencia.setJustificada(Boolean.TRUE.equals(request.getJustificada()));
        return asistenciaRepository.save(asistencia);
    }

    public void eliminar(Long id) {
        if (!asistenciaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asistencia no encontrada");
        }
        asistenciaRepository.deleteById(id);
    }
}
