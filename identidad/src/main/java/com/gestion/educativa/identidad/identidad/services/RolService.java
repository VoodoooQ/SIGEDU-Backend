package com.gestion.educativa.identidad.identidad.services;

import java.util.List;
import java.util.stream.Collectors;
import com.gestion.educativa.identidad.identidad.exceptions.RecursoNoEncontradoException;
import com.gestion.educativa.identidad.identidad.models.dto.RolDto;
import com.gestion.educativa.identidad.identidad.models.entity.Rol;
import com.gestion.educativa.identidad.identidad.models.entity.Usuario;
import com.gestion.educativa.identidad.identidad.models.entity.UsuarioRol;
import com.gestion.educativa.identidad.identidad.models.request.AsignarRolRequest;
import com.gestion.educativa.identidad.identidad.repositories.RolRepository;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRepository;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;

    @Transactional
    public RolDto crearRol(RolDto rolDto) {
        if (rolDto.getNombreRol() == null || rolDto.getNombreRol().isBlank()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }
        if (rolRepository.findByNombreRol(rolDto.getNombreRol()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un rol con el mismo nombre");
        }

        Rol rol = new Rol();
        rol.setNombreRol(rolDto.getNombreRol().trim());
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
    public void asignarRol(AsignarRolRequest solicitud) {
        Usuario usuario = usuarioRepository.findById(solicitud.getRunUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        Rol rol = rolRepository.findById(solicitud.getIdRol())
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado"));

        if (usuarioRolRepository.existsByUsuario_RunUsuarioAndRol_IdRol(solicitud.getRunUsuario(), solicitud.getIdRol())) {
            throw new IllegalArgumentException("El rol ya está asignado al usuario");
        }

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        usuarioRolRepository.save(usuarioRol);
    }

    @Transactional
    public void revocarRol(String runUsuario, Integer idRol) {
        if (!usuarioRolRepository.existsByUsuario_RunUsuarioAndRol_IdRol(runUsuario, idRol)) {
            throw new RecursoNoEncontradoException("La asignación de rol no existe");
        }
        usuarioRolRepository.deleteByUsuario_RunUsuarioAndRol_IdRol(runUsuario, idRol);
    }

    @Transactional(readOnly = true)
    public List<RolDto> obtenerRolesPorUsuario(String runUsuario) {
        if (!usuarioRepository.existsById(runUsuario)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado");
        }

        return usuarioRolRepository.findByUsuario_RunUsuario(runUsuario)
                .stream()
                .map(UsuarioRol::getRol)
                .map(this::mapearRolADto)
                .collect(Collectors.toList());
    }

    private RolDto mapearRolADto(Rol rol) {
        return new RolDto(rol.getIdRol(), rol.getNombreRol());
    }
}
