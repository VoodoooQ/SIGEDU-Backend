package com.gestion.educativa.identidad.identidad.repositories;

import java.util.Optional;
import com.gestion.educativa.identidad.identidad.models.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByCorreoUsuario(String correoUsuario);

    boolean existsByCorreoUsuario(String correoUsuario);
}
