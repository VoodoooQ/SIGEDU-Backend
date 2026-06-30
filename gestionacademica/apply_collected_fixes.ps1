Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Write-Utf8NoBom([string]$Path, [string]$Content) {
  $utf8NoBom = New-Object System.Text.UTF8Encoding($false)
  [System.IO.File]::WriteAllText($Path, $Content, $utf8NoBom)
}

$notaServicePath = 'E:\SIGEDU-Backend\notas\src\main\java\com\gestion\educativa\notas\notas\services\NotaService.java'
$notaServiceContent = @"
package com.gestion.educativa.notas.notas.services;

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
        nota.setTipoEvaluacion(request.getTipoEvaluacion());
        nota.setPonderacion(request.getPonderacion());
        nota.setCalificacion(request.getCalificacion());
        nota.setObservaciones(request.getObservaciones());
        return nota;
    }
}
"@
Write-Utf8NoBom -Path $notaServicePath -Content $notaServiceContent

$reunionServicePath = 'E:\SIGEDU-Backend\reuniones\src\main\java\com\gestion\educativa\reuniones\reuniones\services\ReunionService.java'
$reunionServiceContent = @"
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
"@
Write-Utf8NoBom -Path $reunionServicePath -Content $reunionServiceContent

$anotacionesPath = 'E:\SIGEDU-Backend\convivencia\src\main\java\com\gestion\educativa\convivencia\convivencia\services\impl\AnotacionesServiceImpl.java'
$anotacionesContent = @"
package com.gestion.educativa.convivencia.convivencia.services.impl;

import com.gestion.educativa.convivencia.convivencia.exceptions.ResourceNotFoundException;
import com.gestion.educativa.convivencia.convivencia.models.dto.AnotacionesDto;
import com.gestion.educativa.convivencia.convivencia.models.entity.Anotaciones;
import com.gestion.educativa.convivencia.convivencia.models.request.AnotacionRequest;
import com.gestion.educativa.convivencia.convivencia.repositories.AnotacionesRepository;
import com.gestion.educativa.convivencia.convivencia.services.AnotacionesService;
import com.gestion.educativa.convivencia.convivencia.services.MatriculaClientService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AnotacionesServiceImpl implements AnotacionesService {

    private final AnotacionesRepository repository;
    private final MatriculaClientService matriculaClientService;

    public AnotacionesServiceImpl(AnotacionesRepository repository, MatriculaClientService matriculaClientService) {
        this.repository = repository;
        this.matriculaClientService = matriculaClientService;
    }

    @Override
    public List<AnotacionesDto> findAll() { return repository.findAll().stream().map(this::toDto).collect(Collectors.toList()); }

    @Override
    public AnotacionesDto findById(Long id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Anotacion no encontrada con id: " + id));
    }

    @Override
    public AnotacionesDto create(AnotacionRequest request, String runAutorRef) {
        validarMatriculaActiva(request.getRunEstudianteRef());

        Anotaciones a = new Anotaciones();
        a.setRunEstudianteRef(request.getRunEstudianteRef());
        a.setFecha(request.getFecha());
        a.setTipo(request.getTipo());
        a.setDescripcion(request.getDescripcion());
        a.setRunAutorRef(runAutorRef);
        return toDto(repository.save(a));
    }

    @Override
    public AnotacionesDto update(Long id, AnotacionRequest request) {
        Anotaciones existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotacion no encontrada con id: " + id));
        validarMatriculaActiva(request.getRunEstudianteRef());
        existing.setRunEstudianteRef(request.getRunEstudianteRef());
        existing.setFecha(request.getFecha());
        existing.setTipo(request.getTipo());
        existing.setDescripcion(request.getDescripcion());
        return toDto(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Anotaciones existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotacion no encontrada con id: " + id));
        repository.delete(existing);
    }

    @Override
    public List<AnotacionesDto> findByRunEstudianteRef(String runEstudianteRef) {
        return repository.findByRunEstudianteRef(runEstudianteRef).stream().map(this::toDto).collect(Collectors.toList());
    }

    private void validarMatriculaActiva(String runEstudianteRef) {
        if (!matriculaClientService.estudianteMatriculado(runEstudianteRef)) {
            log.warn("Estudiante {} no registra matricula activa o matricula devolvio lista vacia", runEstudianteRef);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estudiante no registra matricula activa");
        }
    }

    private AnotacionesDto toDto(Anotaciones a) {
        AnotacionesDto d = new AnotacionesDto();
        d.setId(a.getId());
        d.setRunEstudianteRef(a.getRunEstudianteRef());
        d.setFecha(a.getFecha());
        d.setTipo(a.getTipo());
        d.setDescripcion(a.getDescripcion());
        d.setRunAutorRef(a.getRunAutorRef());
        return d;
    }
}
"@
Write-Utf8NoBom -Path $anotacionesPath -Content $anotacionesContent

$requestsPath = 'E:\SIGEDU-Backend\requests.rest'
$backupPath = 'E:\SIGEDU-Backend\requests.rest.bak-20260624-apply2'
Copy-Item -Path $requestsPath -Destination $backupPath -Force
$content = Get-Content -Path $requestsPath -Raw

$replacements = @{
    '@idCurso = 3' = '@idCurso = 4'
    '@idNivel = 3' = '@idNivel = 4'
    '@idPeriodo = 3' = '@idPeriodo = 4'
    '@idSala = 3' = '@idSala = 4'
    '@idDireccion = 2' = '@idDireccion = 3'
    '@idReunionGeneral = 2' = '@idReunionGeneral = 9'
    '@idReunionApoderado = 2' = '@idReunionApoderado = 10'
    '@idReunionP1A1 = 2' = '@idReunionP1A1 = 9'
    '@idAcuerdo = 2' = '@idAcuerdo = 9'
    '@idMatricula = 1' = '@idMatricula = 2'
    '@idAnotacion = 2' = '@idAnotacion = 8'
    '@idHojaVida = 2' = '@idHojaVida = 7'
    '@idNota = 2' = '@idNota = 7'
    '@idEvento = 2' = '@idEvento = 9'
    '@idAntecedenteAcademico = 1' = '@idAntecedenteAcademico = 7'
    '@idAntecedenteApoderado = 1' = '@idAntecedenteApoderado = 5'
    '@idAntecedenteMedico = 1' = '@idAntecedenteMedico = 7'
}
foreach ($pair in $replacements.GetEnumerator()) {
    $content = $content.Replace($pair.Key, $pair.Value)
}

$marker = "# ============================================================`r`n# MS6 notas"
$precondition = @"
### PRECONDICION - Crear asignatura base para notas
POST {{ms7}}/api/asignatura
Authorization: Bearer {{token}}
Content-Type: application/json

{
  \"nombre_asignatura\": \"Lenguaje y Comunicacion Base\",
  \"id_nivel_ref\": {{idNivel}},
  \"run_docente_ref\": \"{{runDocente}}\"
}

# ============================================================
# MS6 notas
"@
if ($content.Contains($marker)) {
    $content = $content.Replace($marker, $precondition)
}

Write-Utf8NoBom -Path $requestsPath -Content $content
Write-Output 'PATCH_OK'
