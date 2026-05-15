package com.gestion.educativa.identidad.identidad.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.gestion.educativa.identidad.identidad.models.entity.Rol;
import com.gestion.educativa.identidad.identidad.models.entity.Usuario;
import com.gestion.educativa.identidad.identidad.models.entity.UsuarioRol;
import com.gestion.educativa.identidad.identidad.repositories.RolRepository;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRepository;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class RoleSeedConfig {

    @Bean
    @Transactional
    public CommandLineRunner sembrarRolesBase(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            UsuarioRolRepository usuarioRolRepository
    ) {
        return args -> {
            List<String> rolesBase = List.of(
                    "ADMIN",
                    "DIRECTIVO",
                    "INSPECTOR",
                    "FUNCIONARIO",
                    "DOCENTE",
                    "APODERADO",
                    "ESTUDIANTE"
            );

            Map<String, Rol> rolesPorNombre = new LinkedHashMap<>();
            for (String nombreRol : rolesBase) {
                Rol rol = rolRepository.findByNombreRolIgnoreCase(nombreRol)
                        .orElseGet(() -> {
                            Rol nuevoRol = new Rol();
                            nuevoRol.setNombreRol(nombreRol);
                            return rolRepository.save(nuevoRol);
                        });
                rolesPorNombre.put(nombreRol, rol);
            }

            // Compatibilidad: si un usuario tiene ADMIN, se le agrega DIRECTIVO.
            Rol rolDirectivo = rolesPorNombre.get("DIRECTIVO");
            if (rolDirectivo == null) {
                return;
            }

            List<UsuarioRol> adminsAsignados = usuarioRolRepository.findByRol_NombreRolIgnoreCase("ADMIN");
            for (UsuarioRol asignacionAdmin : adminsAsignados) {
                String runUsuario = asignacionAdmin.getUsuario().getRunUsuario();
                if (usuarioRolRepository.existsByUsuario_RunUsuarioAndRol_IdRol(runUsuario, rolDirectivo.getIdRol())) {
                    continue;
                }

                Usuario usuario = usuarioRepository.findById(runUsuario).orElse(null);
                if (usuario == null) {
                    continue;
                }

                UsuarioRol asignacionDirectivo = new UsuarioRol();
                asignacionDirectivo.setUsuario(usuario);
                asignacionDirectivo.setRol(rolDirectivo);
                usuarioRolRepository.save(asignacionDirectivo);
            }
        };
    }
}
