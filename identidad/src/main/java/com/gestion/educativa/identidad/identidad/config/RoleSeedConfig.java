package com.gestion.educativa.identidad.identidad.config;

import java.util.List;
import com.gestion.educativa.identidad.identidad.models.entity.Rol;
import com.gestion.educativa.identidad.identidad.repositories.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleSeedConfig {

    @Bean
    public CommandLineRunner sembrarRolesBase(RolRepository rolRepository) {
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

            for (String nombreRol : rolesBase) {
                if (rolRepository.findByNombreRolIgnoreCase(nombreRol).isPresent()) {
                    continue;
                }
                Rol rol = new Rol();
                rol.setNombreRol(nombreRol);
                rolRepository.save(rol);
            }
        };
    }
}
