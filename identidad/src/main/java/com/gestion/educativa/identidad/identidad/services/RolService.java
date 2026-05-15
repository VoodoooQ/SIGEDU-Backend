package com.gestion.educativa.identidad.identidad.services;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import com.gestion.educativa.identidad.identidad.exceptions.RecursoNoEncontradoException;
import com.gestion.educativa.identidad.identidad.models.dto.RolDto;
import com.gestion.educativa.identidad.identidad.models.entity.Rol;
import com.gestion.educativa.identidad.identidad.models.entity.Usuario;
import com.gestion.educativa.identidad.identidad.models.entity.UsuarioRol;
import com.gestion.educativa.identidad.identidad.repositories.RolRepository;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRepository;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RolService {

    private static final Set<String> ROLES_PRIMORDIALES = Set.of(
            "ADMIN",
            "DIRECTIVO",
            "INSPECTOR",
            "FUNCIONARIO",
            "DOCENTE",
            "APODERADO",
            "ESTUDIANTE"
    );

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;

    @Transactional
    public RolDto crearRol(RolDto rolDto) {
        if (rolDto == null) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }
        return crearRol(rolDto.getNombreRol());
    }

    @Transactional
    public RolDto crearRol(String nombreRol) {
        String nombreRolNormalizado = normalizarNombreRol(nombreRol);
        if (rolRepository.findByNombreRolIgnoreCase(nombreRolNormalizado).isPresent()) {
            throw new IllegalArgumentException("Ya existe un rol con el mismo nombre");
        }

        Rol rol = new Rol();
        rol.setNombreRol(nombreRolNormalizado);
        Rol rolGuardado = rolRepository.save(rol);
        return mapearRolADto(rolGuardado);
    }

    @Transactional(readOnly = true)
    public List<RolDto> listarRoles() {
        return rolRepository.findAll()
                .stream()
                .map(this::mapearRolADto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void asignarRol(String runUsuario, Character dvSolicitado, Integer idRol) {
        Usuario usuario = obtenerUsuarioValidadoPorDv(runUsuario, dvSolicitado);
        asignarRolValidado(usuario.getRunUsuario(), idRol);
    }

    @Transactional
    public void revocarRol(String runUsuario, Character dvSolicitado, Integer idRol) {
        Usuario usuario = obtenerUsuarioValidadoPorDv(runUsuario, dvSolicitado);
        revocarRolValidado(usuario.getRunUsuario(), idRol);
    }

    @Transactional
    public void eliminarRol(Integer idRol) {
        if (idRol == null) {
            throw new IllegalArgumentException("Id de rol obligatorio");
        }

        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado"));

        if (esRolPrimordial(rol.getNombreRol())) {
            throw new IllegalArgumentException("No se puede eliminar un rol primordial del sistema");
        }

        usuarioRolRepository.deleteByRol_IdRol(idRol);
        rolRepository.delete(rol);
    }

    @Transactional(readOnly = true)
    public List<RolDto> obtenerRolesPorUsuario(String runUsuario, Character dvSolicitado) {
        Usuario usuario = obtenerUsuarioValidadoPorDv(runUsuario, dvSolicitado);
        return obtenerRolesPorUsuarioValidado(usuario.getRunUsuario());
    }

    private void asignarRolValidado(String runUsuario, Integer idRol) {
        String runNormalizado = limpiarRun(runUsuario);
        if (runNormalizado == null || runNormalizado.isBlank()) {
            throw new IllegalArgumentException("RUN obligatorio");
        }
        if (idRol == null) {
            throw new IllegalArgumentException("Id de rol obligatorio");
        }

        Usuario usuario = usuarioRepository.findById(runNormalizado)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado"));

        if (usuarioRolRepository.existsByUsuario_RunUsuarioAndRol_IdRol(runNormalizado, idRol)) {
            throw new IllegalArgumentException("El rol ya esta asignado al usuario");
        }

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        usuarioRolRepository.save(usuarioRol);
    }

    private void revocarRolValidado(String runUsuario, Integer idRol) {
        if (!usuarioRolRepository.existsByUsuario_RunUsuarioAndRol_IdRol(runUsuario, idRol)) {
            throw new RecursoNoEncontradoException("La asignacion de rol no existe");
        }
        usuarioRolRepository.deleteByUsuario_RunUsuarioAndRol_IdRol(runUsuario, idRol);
    }

    private List<RolDto> obtenerRolesPorUsuarioValidado(String runUsuario) {
        if (!usuarioRepository.existsById(runUsuario)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado");
        }

        return usuarioRolRepository.findByUsuario_RunUsuario(runUsuario)
                .stream()
                .map(UsuarioRol::getRol)
                .map(this::mapearRolADto)
                .collect(Collectors.toList());
    }

    private Usuario obtenerUsuarioValidadoPorDv(String runUsuario, Character dvSolicitado) {
        String runNormalizado = limpiarRun(runUsuario);
        if (runNormalizado == null || runNormalizado.isBlank()) {
            throw new IllegalArgumentException("RUN obligatorio");
        }
        if (dvSolicitado == null) {
            throw new IllegalArgumentException("DV obligatorio");
        }

        Usuario usuario = usuarioRepository.findById(runNormalizado)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        char dvNormalizado = Character.toUpperCase(dvSolicitado);
        if (Character.toUpperCase(usuario.getDvrunUsuario()) != dvNormalizado) {
            throw new IllegalArgumentException("DV no coincide con el RUN indicado");
        }
        return usuario;
    }

    private String limpiarRun(String runUsuario) {
        if (runUsuario == null) {
            return null;
        }
        return runUsuario.replaceAll("[^0-9]", "").trim();
    }

    private String normalizarNombreRol(String nombreRol) {
        if (nombreRol == null || nombreRol.isBlank()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }
        return nombreRol.trim().toUpperCase();
    }

    private boolean esRolPrimordial(String nombreRol) {
        if (nombreRol == null) {
            return false;
        }
        return ROLES_PRIMORDIALES.contains(nombreRol.trim().toUpperCase(Locale.ROOT));
    }

    private RolDto mapearRolADto(Rol rol) {
        return new RolDto(rol.getIdRol(), rol.getNombreRol());
    }
}
