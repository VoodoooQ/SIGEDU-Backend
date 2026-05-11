package com.gestion.educativa.identidad.identidad.services;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import com.gestion.educativa.identidad.identidad.exceptions.RecursoNoEncontradoException;
import com.gestion.educativa.identidad.identidad.models.dto.UsuarioDto;
import com.gestion.educativa.identidad.identidad.models.entity.Apoderado;
import com.gestion.educativa.identidad.identidad.models.entity.Directivo;
import com.gestion.educativa.identidad.identidad.models.entity.Docente;
import com.gestion.educativa.identidad.identidad.models.entity.Estudiante;
import com.gestion.educativa.identidad.identidad.models.entity.Funcionario;
import com.gestion.educativa.identidad.identidad.models.entity.Inspector;
import com.gestion.educativa.identidad.identidad.models.entity.Rol;
import com.gestion.educativa.identidad.identidad.models.entity.Usuario;
import com.gestion.educativa.identidad.identidad.models.entity.UsuarioRol;
import com.gestion.educativa.identidad.identidad.models.request.UsuarioRequest;
import com.gestion.educativa.identidad.identidad.repositories.ApoderadoRepository;
import com.gestion.educativa.identidad.identidad.repositories.DirectivoRepository;
import com.gestion.educativa.identidad.identidad.repositories.DocenteRepository;
import com.gestion.educativa.identidad.identidad.repositories.EstudianteRepository;
import com.gestion.educativa.identidad.identidad.repositories.FuncionarioRepository;
import com.gestion.educativa.identidad.identidad.repositories.InspectorRepository;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final DocenteRepository docenteRepository;
    private final InspectorRepository inspectorRepository;
    private final DirectivoRepository directivoRepository;
    private final ApoderadoRepository apoderadoRepository;
    private final EstudianteRepository estudianteRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDto crearUsuario(UsuarioRequest solicitud) {
        String runNormalizado = limpiarRun(solicitud.getRunUsuario());
        String correoNormalizado = solicitud.getCorreoUsuario().trim().toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsById(runNormalizado) || usuarioRepository.existsByCorreoUsuario(correoNormalizado)) {
            throw new DataIntegrityViolationException("RUT o correo ya registrado");
        }

        char dvNormalizado = Character.toUpperCase(solicitud.getDvrunUsuario());
        if (!validarRutChileno(runNormalizado, dvNormalizado)) {
            throw new IllegalArgumentException("RUT chileno inválido");
        }

        Usuario usuario = new Usuario();
        usuario.setRunUsuario(runNormalizado);
        usuario.setDvrunUsuario(dvNormalizado);
        usuario.setPNombreUsuario(solicitud.getPNombreUsuario().trim());
        usuario.setOsNombreUsuario(normalizarTextoOpcional(solicitud.getOsNombreUsuario()));
        usuario.setPApellidoUsuario(solicitud.getPApellidoUsuario().trim());
        usuario.setOsApellidoUsuario(normalizarTextoOpcional(solicitud.getOsApellidoUsuario()));
        usuario.setCorreoUsuario(correoNormalizado);
        usuario.setTelefonoUsuario(normalizarTextoOpcional(solicitud.getTelefonoUsuario()));
        usuario.setGenero(Character.toUpperCase(solicitud.getGenero()));
        usuario.setContrasena(passwordEncoder.encode(solicitud.getContrasena()));
        usuarioRepository.save(usuario);

        crearSubtipoSegunSolicitud(usuario, solicitud);

        return mapearUsuarioADto(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioDto obtenerUsuario(String runUsuario) {
        Usuario usuario = usuarioRepository.findById(limpiarRun(runUsuario))
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        return mapearUsuarioADto(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapearUsuarioADto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioDto actualizarUsuario(String runUsuario, UsuarioRequest solicitud) {
        String runNormalizado = limpiarRun(runUsuario);
        Usuario usuario = usuarioRepository.findById(runNormalizado)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        if (solicitud.getRunUsuario() != null && !solicitud.getRunUsuario().isBlank()) {
            String runSolicitud = limpiarRun(solicitud.getRunUsuario());
            if (!runNormalizado.equals(runSolicitud)) {
                throw new IllegalArgumentException("El RUN del cuerpo debe coincidir con el RUN de la ruta");
            }
        }

        if (solicitud.getDvrunUsuario() != null) {
            char dvNormalizado = Character.toUpperCase(solicitud.getDvrunUsuario());
            if (!validarRutChileno(runNormalizado, dvNormalizado)) {
                throw new IllegalArgumentException("RUT chileno inválido");
            }
            usuario.setDvrunUsuario(dvNormalizado);
        }

        if (solicitud.getPNombreUsuario() != null) {
            usuario.setPNombreUsuario(solicitud.getPNombreUsuario().trim());
        }
        if (solicitud.getOsNombreUsuario() != null) {
            usuario.setOsNombreUsuario(normalizarTextoOpcional(solicitud.getOsNombreUsuario()));
        }
        if (solicitud.getPApellidoUsuario() != null) {
            usuario.setPApellidoUsuario(solicitud.getPApellidoUsuario().trim());
        }
        if (solicitud.getOsApellidoUsuario() != null) {
            usuario.setOsApellidoUsuario(normalizarTextoOpcional(solicitud.getOsApellidoUsuario()));
        }
        if (solicitud.getCorreoUsuario() != null) {
            String correoNormalizado = solicitud.getCorreoUsuario().trim().toLowerCase(Locale.ROOT);
            if (!correoNormalizado.equals(usuario.getCorreoUsuario()) && usuarioRepository.existsByCorreoUsuario(correoNormalizado)) {
                throw new DataIntegrityViolationException("RUT o correo ya registrado");
            }
            usuario.setCorreoUsuario(correoNormalizado);
        }
        if (solicitud.getTelefonoUsuario() != null) {
            usuario.setTelefonoUsuario(normalizarTextoOpcional(solicitud.getTelefonoUsuario()));
        }
        if (solicitud.getGenero() != null) {
            usuario.setGenero(Character.toUpperCase(solicitud.getGenero()));
        }
        if (solicitud.getContrasena() != null && !solicitud.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(solicitud.getContrasena()));
        }

        usuarioRepository.save(usuario);
        actualizarSubtipoSiCorresponde(runNormalizado, solicitud);
        return mapearUsuarioADto(usuario);
    }

    @Transactional
    public void eliminarUsuario(String runUsuario) {
        String runNormalizado = limpiarRun(runUsuario);
        if (!usuarioRepository.existsById(runNormalizado)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(runNormalizado);
    }

    private void crearSubtipoSegunSolicitud(Usuario usuario, UsuarioRequest solicitud) {
        String tipoUsuario = solicitud.getTipoUsuario().trim().toUpperCase(Locale.ROOT);
        String campoEspecifico = normalizarTextoOpcional(solicitud.getCampoEspecifico());

        switch (tipoUsuario) {
            case "DOCENTE" -> {
                Funcionario funcionario = new Funcionario();
                funcionario.setRunUsuario(usuario.getRunUsuario());
                funcionario.setTitulo("DOCENTE");
                funcionario.setUsuario(usuario);
                funcionarioRepository.save(funcionario);

                Docente docente = new Docente();
                docente.setRunUsuario(usuario.getRunUsuario());
                docente.setEspecialidad(obtenerCampoEspecificoObligatorio(campoEspecifico, "especialidad"));
                docente.setFuncionario(funcionario);
                docenteRepository.save(docente);
            }
            case "INSPECTOR" -> {
                Funcionario funcionario = new Funcionario();
                funcionario.setRunUsuario(usuario.getRunUsuario());
                funcionario.setTitulo("INSPECTOR");
                funcionario.setUsuario(usuario);
                funcionarioRepository.save(funcionario);

                Inspector inspector = new Inspector();
                inspector.setRunUsuario(usuario.getRunUsuario());
                inspector.setArea(obtenerCampoEspecificoObligatorio(campoEspecifico, "área"));
                inspector.setFuncionario(funcionario);
                inspectorRepository.save(inspector);
            }
            case "DIRECTIVO" -> {
                Funcionario funcionario = new Funcionario();
                funcionario.setRunUsuario(usuario.getRunUsuario());
                funcionario.setTitulo("DIRECTIVO");
                funcionario.setUsuario(usuario);
                funcionarioRepository.save(funcionario);

                Directivo directivo = new Directivo();
                directivo.setRunUsuario(usuario.getRunUsuario());
                directivo.setCargo(obtenerCampoEspecificoObligatorio(campoEspecifico, "cargo"));
                directivo.setFuncionario(funcionario);
                directivoRepository.save(directivo);
            }
            case "APODERADO" -> {
                Apoderado apoderado = new Apoderado();
                apoderado.setRunUsuario(usuario.getRunUsuario());
                apoderado.setParentesco(obtenerCampoEspecificoObligatorio(campoEspecifico, "parentesco"));
                apoderado.setUsuario(usuario);
                apoderadoRepository.save(apoderado);
            }
            case "ESTUDIANTE" -> {
                String runApoderado = limpiarRun(solicitud.getRunApoderado());
                if (runApoderado == null || runApoderado.isBlank()) {
                    throw new IllegalArgumentException("El runApoderado es obligatorio para estudiantes");
                }

                Apoderado apoderado = apoderadoRepository.findById(runApoderado)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Apoderado no encontrado"));

                Estudiante estudiante = new Estudiante();
                estudiante.setRunUsuario(usuario.getRunUsuario());
                estudiante.setParentesco(obtenerCampoEspecificoObligatorio(campoEspecifico, "parentesco"));
                estudiante.setUsuario(usuario);
                estudiante.setApoderado(apoderado);
                estudianteRepository.save(estudiante);
            }
            default -> throw new IllegalArgumentException("Tipo de usuario no válido");
        }
    }

    private void actualizarSubtipoSiCorresponde(String runUsuario, UsuarioRequest solicitud) {
        if (solicitud.getTipoUsuario() == null || solicitud.getTipoUsuario().isBlank()) {
            return;
        }

        String tipoUsuario = solicitud.getTipoUsuario().trim().toUpperCase(Locale.ROOT);
        String campoEspecifico = normalizarTextoOpcional(solicitud.getCampoEspecifico());

        switch (tipoUsuario) {
            case "DOCENTE" -> {
                Docente docente = docenteRepository.findById(runUsuario)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Docente no encontrado"));
                if (campoEspecifico != null) {
                    docente.setEspecialidad(campoEspecifico);
                    docenteRepository.save(docente);
                }
            }
            case "INSPECTOR" -> {
                Inspector inspector = inspectorRepository.findById(runUsuario)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Inspector no encontrado"));
                if (campoEspecifico != null) {
                    inspector.setArea(campoEspecifico);
                    inspectorRepository.save(inspector);
                }
            }
            case "DIRECTIVO" -> {
                Directivo directivo = directivoRepository.findById(runUsuario)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Directivo no encontrado"));
                if (campoEspecifico != null) {
                    directivo.setCargo(campoEspecifico);
                    directivoRepository.save(directivo);
                }
            }
            case "APODERADO" -> {
                Apoderado apoderado = apoderadoRepository.findById(runUsuario)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Apoderado no encontrado"));
                if (campoEspecifico != null) {
                    apoderado.setParentesco(campoEspecifico);
                    apoderadoRepository.save(apoderado);
                }
            }
            case "ESTUDIANTE" -> {
                Estudiante estudiante = estudianteRepository.findById(runUsuario)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado"));
                if (campoEspecifico != null) {
                    estudiante.setParentesco(campoEspecifico);
                }
                String runApoderado = limpiarRun(solicitud.getRunApoderado());
                if (runApoderado != null && !runApoderado.isBlank()) {
                    Apoderado apoderado = apoderadoRepository.findById(runApoderado)
                            .orElseThrow(() -> new RecursoNoEncontradoException("Apoderado no encontrado"));
                    estudiante.setApoderado(apoderado);
                }
                estudianteRepository.save(estudiante);
            }
            default -> throw new IllegalArgumentException("Tipo de usuario no válido");
        }
    }

    private UsuarioDto mapearUsuarioADto(Usuario usuario) {
        List<String> roles = usuario.getRoles() == null
                ? List.of()
                : usuario.getRoles().stream()
                        .map(UsuarioRol::getRol)
                        .filter(Objects::nonNull)
                        .map(Rol::getNombreRol)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        return new UsuarioDto(
                usuario.getRunUsuario(),
                usuario.getDvrunUsuario(),
                usuario.getPNombreUsuario(),
                usuario.getOsNombreUsuario(),
                usuario.getPApellidoUsuario(),
                usuario.getOsApellidoUsuario(),
                usuario.getCorreoUsuario(),
                usuario.getTelefonoUsuario(),
                usuario.getGenero(),
                roles
        );
    }

    private String limpiarRun(String runUsuario) {
        if (runUsuario == null) {
            return null;
        }
        return runUsuario.replaceAll("[^0-9]", "").trim();
    }

    private String normalizarTextoOpcional(String texto) {
        if (texto == null) {
            return null;
        }
        String valor = texto.trim();
        return valor.isEmpty() ? null : valor;
    }

    private String obtenerCampoEspecificoObligatorio(String campoEspecifico, String nombreCampo) {
        if (campoEspecifico == null || campoEspecifico.isBlank()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " es obligatorio para el tipo seleccionado");
        }
        return campoEspecifico;
    }

    private boolean validarRutChileno(String run, char dv) {
        if (run == null || run.isBlank() || !run.matches("\\d+")) {
            return false;
        }

        int suma = 0;
        int multiplicador = 2;
        for (int i = run.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(run.charAt(i));
            suma += digito * multiplicador;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
        }

        int resultado = 11 - (suma % 11);
        char dvCalculado;
        if (resultado == 11) {
            dvCalculado = '0';
        } else if (resultado == 10) {
            dvCalculado = 'K';
        } else {
            dvCalculado = Character.forDigit(resultado, 10);
        }

        return Character.toUpperCase(dv) == dvCalculado;
    }
}
