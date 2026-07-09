package com.gestion.educativa.identidad.identidad.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
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
import com.gestion.educativa.identidad.identidad.models.request.ActualizarUsuarioRequest;
import com.gestion.educativa.identidad.identidad.models.request.CrearUsuarioRequest;
import com.gestion.educativa.identidad.identidad.repositories.ApoderadoRepository;
import com.gestion.educativa.identidad.identidad.repositories.EstudianteRepository;
import com.gestion.educativa.identidad.identidad.repositories.RolRepository;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRepository;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Set<String> ROLES_LECTURA_GLOBAL = Set.of("ADMIN", "DIRECTIVO", "INSPECTOR", "DOCENTE");
    private static final Set<String> ROLES_LECTURA_PROPIA = Set.of("FUNCIONARIO", "ESTUDIANTE");

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final ApoderadoRepository apoderadoRepository;
    private final EstudianteRepository estudianteRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDto crearUsuario(CrearUsuarioRequest solicitud) {
        String runNormalizado = limpiarRun(solicitud.getRunUsuario());
        String correoNormalizado = normalizarCorreo(solicitud.getCorreoUsuario());
        String tipoUsuario = normalizarTipoUsuario(solicitud.getTipoUsuario());

        validarPermisoCreacion();
        validarDisponibilidad(runNormalizado, correoNormalizado);
        validarRutChileno(runNormalizado, solicitud.getDvrunUsuario());

        Usuario nuevoUsuario = construirEntidadSegunTipo(solicitud, runNormalizado, correoNormalizado, tipoUsuario);
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        asignarRolInicial(usuarioGuardado, tipoUsuario);
        return mapearUsuarioADto(usuarioGuardado);
    }

    @Transactional(readOnly = true)
    public UsuarioDto obtenerUsuario(String runUsuario, Character dvSolicitado) {
        String runNormalizado = limpiarRun(runUsuario);
        Authentication autenticacion = obtenerAutenticacion();
        String runSolicitante = autenticacion.getName();
        Set<String> autoridades = obtenerAutoridades(autenticacion);

        if (dvSolicitado == null) {
            throw new IllegalArgumentException("DV obligatorio para consultar");
        }

        Usuario usuario = usuarioRepository.findById(runNormalizado)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        char dvNormalizado = Character.toUpperCase(dvSolicitado);
        if (Character.toUpperCase(usuario.getDvrunUsuario()) != dvNormalizado) {
            throw new IllegalArgumentException("DV no coincide con el RUN indicado");
        }

        validarPermisoLectura(runNormalizado);
        return mapearUsuarioSegunContexto(usuario, autoridades, runSolicitante);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> listarUsuarios() {
        Authentication autenticacion = obtenerAutenticacion();
        String runSolicitante = autenticacion.getName();
        Set<String> autoridades = obtenerAutoridades(autenticacion);

        if (tieneAlgunaAutoridad(autoridades, ROLES_LECTURA_GLOBAL)) {
            return usuarioRepository.findAll()
                    .stream()
                    .map(usuario -> mapearUsuarioSegunContexto(usuario, autoridades, runSolicitante))
                    .collect(Collectors.toList());
        }

        throw new AccessDeniedException("No tienes permisos para listar usuarios");
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> listarMisEstudiantes() {
        Authentication autenticacion = obtenerAutenticacion();
        String runSolicitante = autenticacion.getName();
        Set<String> autoridades = obtenerAutoridades(autenticacion);

        if (!autoridades.contains("APODERADO")) {
            throw new AccessDeniedException("Solo apoderados pueden consultar sus estudiantes vinculados");
        }

        return estudianteRepository.findByApoderado_RunUsuario(runSolicitante)
                .stream()
                .map(estudiante -> mapearUsuarioSegunContexto(estudiante, autoridades, runSolicitante))
                .collect(Collectors.toList());
    }

    
    @Transactional(readOnly = true)
    public UsuarioDto obtenerPerfilPropio() {
        String runSolicitante = obtenerAutenticacion().getName();
        Usuario usuario = usuarioRepository.findById(runSolicitante)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        return mapearUsuarioADto(usuario);
    }

    @Transactional
    public UsuarioDto actualizarUsuario(String runUsuario, Character dvSolicitado, ActualizarUsuarioRequest solicitud) {
        String runNormalizado = limpiarRun(runUsuario);
        validarPermisoActualizacion(runNormalizado);

        if (dvSolicitado == null) {
            throw new IllegalArgumentException("DV obligatorio para actualizar");
        }

        Usuario usuario = usuarioRepository.findById(runNormalizado)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        char dvNormalizado = Character.toUpperCase(dvSolicitado);
        if (Character.toUpperCase(usuario.getDvrunUsuario()) != dvNormalizado) {
            throw new IllegalArgumentException("DV no coincide con el RUN indicado");
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
            String correoNormalizado = normalizarCorreo(solicitud.getCorreoUsuario());
            if (!correoNormalizado.equalsIgnoreCase(usuario.getCorreoUsuario())
                    && usuarioRepository.existsByCorreoUsuario(correoNormalizado)) {
                throw new DataIntegrityViolationException("Correo ya registrado");
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
            usuario.setContrasena(passwordEncoder.encode(solicitud.getContrasena().trim()));
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return mapearUsuarioADto(usuarioActualizado);
    }

    @Transactional
    public void eliminarUsuario(String runUsuario, Character dvSolicitado) {
        String runNormalizado = limpiarRun(runUsuario);
        validarPermisoEliminacion();

        if (dvSolicitado == null) {
            throw new IllegalArgumentException("DV obligatorio para eliminar");
        }

        Usuario usuario = usuarioRepository.findById(runNormalizado)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        char dvNormalizado = Character.toUpperCase(dvSolicitado);
        if (Character.toUpperCase(usuario.getDvrunUsuario()) != dvNormalizado) {
            throw new IllegalArgumentException("DV no coincide con el RUN indicado");
        }

        usuarioRepository.delete(usuario);
    }

    private void validarPermisoCreacion() {
        Set<String> autoridades = obtenerAutoridades(obtenerAutenticacion());

        if (tieneAlgunaAutoridad(autoridades, Set.of("ADMIN", "DIRECTIVO"))) {
            return;
        }

        if (autoridades.contains("INSPECTOR")) {
            return;
        }

        throw new AccessDeniedException("No tienes permisos para crear usuarios");
    }

    private void validarPermisoLectura(String runObjetivo) {
        Authentication autenticacion = obtenerAutenticacion();
        String runSolicitante = autenticacion.getName();
        Set<String> autoridades = obtenerAutoridades(autenticacion);

        if (tieneAlgunaAutoridad(autoridades, ROLES_LECTURA_GLOBAL)) {
            return;
        }

        if (tieneAlgunaAutoridad(autoridades, ROLES_LECTURA_PROPIA) && runSolicitante.equals(runObjetivo)) {
            return;
        }

        if (autoridades.contains("APODERADO")
                && (runSolicitante.equals(runObjetivo) || esEstudianteAsociado(runSolicitante, runObjetivo))) {
            return;
        }

        throw new AccessDeniedException("No tienes permisos para consultar este usuario");
    }

    private void validarPermisoActualizacion(String runObjetivo) {
        Authentication autenticacion = obtenerAutenticacion();
        String runSolicitante = autenticacion.getName();
        Set<String> autoridades = obtenerAutoridades(autenticacion);

        if (tieneAlgunaAutoridad(autoridades, Set.of("ADMIN", "DIRECTIVO"))) {
            return;
        }

        if (autoridades.contains("INSPECTOR")) {
            return;
        }

        if (autoridades.contains("FUNCIONARIO") && runSolicitante.equals(runObjetivo)) {
            return;
        }

        throw new AccessDeniedException("No tienes permisos para actualizar este usuario");
    }

    private void validarPermisoEliminacion() {
        Set<String> autoridades = obtenerAutoridades(obtenerAutenticacion());
        if (!tieneAlgunaAutoridad(autoridades, Set.of("ADMIN", "DIRECTIVO"))) {
            throw new AccessDeniedException("No tienes permisos para eliminar usuarios");
        }
    }

    private Usuario construirEntidadSegunTipo(
            CrearUsuarioRequest solicitud,
            String runNormalizado,
            String correoNormalizado,
            String tipoUsuario
    ) {
        String campoEspecifico = normalizarTextoOpcional(solicitud.getCampoEspecifico());
        Usuario usuario;

        switch (tipoUsuario) {
            case "FUNCIONARIO" -> {
                Funcionario funcionario = new Funcionario();
                funcionario.setTitulo(obtenerCampoEspecificoObligatorio(campoEspecifico, "titulo"));
                usuario = funcionario;
            }
            case "DOCENTE" -> {
                Docente docente = new Docente();
                docente.setTitulo("DOCENTE");
                docente.setEspecialidad(obtenerCampoEspecificoObligatorio(campoEspecifico, "especialidad"));
                usuario = docente;
            }
            case "INSPECTOR" -> {
                Inspector inspector = new Inspector();
                inspector.setTitulo("INSPECTOR");
                inspector.setArea(obtenerCampoEspecificoObligatorio(campoEspecifico, "area"));
                usuario = inspector;
            }
            case "DIRECTIVO" -> {
                Directivo directivo = new Directivo();
                directivo.setTitulo("DIRECTIVO");
                directivo.setCargo(obtenerCampoEspecificoObligatorio(campoEspecifico, "cargo"));
                usuario = directivo;
            }
            case "APODERADO" -> {
                Apoderado apoderado = new Apoderado();
                apoderado.setParentesco(obtenerCampoEspecificoObligatorio(campoEspecifico, "parentesco"));
                usuario = apoderado;
            }
            case "ESTUDIANTE" -> {
                String runApoderadoNormalizado = limpiarRun(solicitud.getRunApoderado());
                if (runApoderadoNormalizado == null || runApoderadoNormalizado.isBlank()) {
                    throw new IllegalArgumentException("runApoderado es obligatorio para tipo ESTUDIANTE");
                }
                Apoderado apoderado = apoderadoRepository.findById(runApoderadoNormalizado)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Apoderado no encontrado"));
                Estudiante estudiante = new Estudiante();
                estudiante.setParentesco(obtenerCampoEspecificoObligatorio(campoEspecifico, "parentesco"));
                estudiante.setApoderado(apoderado);
                usuario = estudiante;
            }
            default -> throw new IllegalArgumentException("Tipo de usuario no valido");
        }

        popularDatosBase(usuario, solicitud, runNormalizado, correoNormalizado);
        return usuario;
    }

    private void popularDatosBase(
            Usuario usuario,
            CrearUsuarioRequest solicitud,
            String runNormalizado,
            String correoNormalizado
    ) {
        char dvNormalizado = Character.toUpperCase(solicitud.getDvrunUsuario());
        char generoNormalizado = Character.toUpperCase(solicitud.getGenero());

        usuario.setRunUsuario(runNormalizado);
        usuario.setDvrunUsuario(dvNormalizado);
        usuario.setPNombreUsuario(solicitud.getPNombreUsuario().trim());
        usuario.setOsNombreUsuario(normalizarTextoOpcional(solicitud.getOsNombreUsuario()));
        usuario.setPApellidoUsuario(solicitud.getPApellidoUsuario().trim());
        usuario.setOsApellidoUsuario(normalizarTextoOpcional(solicitud.getOsApellidoUsuario()));
        usuario.setCorreoUsuario(correoNormalizado);
        usuario.setTelefonoUsuario(normalizarTextoOpcional(solicitud.getTelefonoUsuario()));
        usuario.setGenero(generoNormalizado);
        usuario.setContrasena(passwordEncoder.encode(solicitud.getContrasena().trim()));
    }

    private void asignarRolInicial(Usuario usuario, String tipoUsuario) {
        String nombreRol = "DIRECTIVO".equals(tipoUsuario) ? resolverRolDirectivo() : tipoUsuario;
        Rol rol = rolRepository.findByNombreRolIgnoreCase(nombreRol)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado: " + nombreRol));

        if (usuarioRolRepository.existsByUsuario_RunUsuarioAndRol_IdRol(usuario.getRunUsuario(), rol.getIdRol())) {
            return;
        }

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        usuarioRolRepository.save(usuarioRol);

        if (usuario.getRoles() == null) {
            usuario.setRoles(new ArrayList<>());
        }
        usuario.getRoles().add(usuarioRol);
    }

    private String resolverRolDirectivo() {
        if (rolRepository.findByNombreRolIgnoreCase("DIRECTIVO").isPresent()) {
            return "DIRECTIVO";
        }
        if (rolRepository.findByNombreRolIgnoreCase("ADMIN").isPresent()) {
            return "ADMIN";
        }
        throw new RecursoNoEncontradoException("No existe rol DIRECTIVO ni ADMIN para asignar");
    }

    private boolean esEstudianteAsociado(String runApoderado, String runEstudiante) {
        return estudianteRepository.findById(runEstudiante)
                .map(Estudiante::getApoderado)
                .filter(Objects::nonNull)
                .map(Usuario::getRunUsuario)
                .filter(runApoderado::equals)
                .isPresent();
    }

    private Authentication obtenerAutenticacion() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !autenticacion.isAuthenticated()
                || "anonymousUser".equals(autenticacion.getPrincipal())) {
            throw new AccessDeniedException("Autenticacion requerida");
        }
        return autenticacion;
    }

    private Set<String> obtenerAutoridades(Authentication autenticacion) {
        return autenticacion.getAuthorities()
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase(Locale.ROOT))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private boolean tieneAlgunaAutoridad(Set<String> autoridadesActuales, Set<String> autoridadesBuscadas) {
        return autoridadesActuales.stream().anyMatch(autoridadesBuscadas::contains);
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

    private UsuarioDto mapearUsuarioSegunContexto(Usuario usuario, Set<String> autoridades, String runSolicitante) {
        UsuarioDto base = mapearUsuarioADto(usuario);
        boolean esPropio = usuario.getRunUsuario().equals(runSolicitante);

        if (tieneAlgunaAutoridad(autoridades, ROLES_LECTURA_GLOBAL)) {
            return base;
        }

        if (autoridades.contains("FUNCIONARIO") && esPropio) {
            return base;
        }

        if (autoridades.contains("APODERADO")) {
            return mapearVistaApoderado(base);
        }

        if (autoridades.contains("ESTUDIANTE") && esPropio) {
            return mapearVistaEstudiante(base);
        }

        return base;
    }

    private UsuarioDto mapearVistaApoderado(UsuarioDto base) {
        return new UsuarioDto(
                base.getRunUsuario(),
                base.getDvrunUsuario(),
                base.getPNombreUsuario(),
                base.getOsNombreUsuario(),
                base.getPApellidoUsuario(),
                base.getOsApellidoUsuario(),
                null,
                null,
                base.getGenero(),
                List.of()
        );
    }

    private UsuarioDto mapearVistaEstudiante(UsuarioDto base) {
        return new UsuarioDto(
                base.getRunUsuario(),
                base.getDvrunUsuario(),
                base.getPNombreUsuario(),
                null,
                base.getPApellidoUsuario(),
                null,
                null,
                null,
                null,
                List.of()
        );
    }

    private void validarDisponibilidad(String runNormalizado, String correoNormalizado) {
        if (runNormalizado == null || runNormalizado.isBlank()) {
            throw new IllegalArgumentException("RUN obligatorio");
        }
        if (usuarioRepository.existsById(runNormalizado) || usuarioRepository.existsByCorreoUsuario(correoNormalizado)) {
            throw new DataIntegrityViolationException("RUN o correo ya registrado");
        }
    }

    private String limpiarRun(String runUsuario) {
        if (runUsuario == null) {
            return null;
        }
        return runUsuario.replaceAll("[^0-9]", "").trim();
    }

    private String normalizarCorreo(String correoUsuario) {
        if (correoUsuario == null || correoUsuario.isBlank()) {
            throw new IllegalArgumentException("Correo obligatorio");
        }
        return correoUsuario.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizarTipoUsuario(String tipoUsuario) {
        if (tipoUsuario == null || tipoUsuario.isBlank()) {
            throw new IllegalArgumentException("tipoUsuario obligatorio");
        }
        return tipoUsuario.trim().toUpperCase(Locale.ROOT);
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
            throw new IllegalArgumentException("Campo obligatorio para tipo seleccionado: " + nombreCampo);
        }
        return campoEspecifico;
    }

    private void validarRutChileno(String run, char dv) {
        if (run == null || run.isBlank() || !run.matches("\\d+")) {
            throw new IllegalArgumentException("RUN invalido");
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

        if (Character.toUpperCase(dv) != dvCalculado) {
            throw new IllegalArgumentException("RUT chileno invalido");
        }
    }
}

