package com.gestion.educativa.convivencia.convivencia.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioValidadoDto {
    private String runUsuario;
    private List<String> roles;
}