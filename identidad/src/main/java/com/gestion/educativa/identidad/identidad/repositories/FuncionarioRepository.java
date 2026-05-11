package com.gestion.educativa.identidad.identidad.repositories;

import com.gestion.educativa.identidad.identidad.models.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {
}
