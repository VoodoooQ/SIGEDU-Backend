package com.gestion.educativa.identidad.identidad.repositories;

import java.util.List;
import com.gestion.educativa.identidad.identidad.models.entity.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, Integer> {

    List<UsuarioRol> findByUsuario_RunUsuario(String runUsuario);

    boolean existsByUsuario_RunUsuarioAndRol_IdRol(String runUsuario, Integer idRol);

    void deleteByUsuario_RunUsuarioAndRol_IdRol(String runUsuario, Integer idRol);
}
