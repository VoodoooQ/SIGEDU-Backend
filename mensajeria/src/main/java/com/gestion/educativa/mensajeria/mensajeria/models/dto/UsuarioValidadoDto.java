package com.gestion.educativa.mensajeria.mensajeria.models.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioValidadoDto {

    private String runUsuario;
    private List<String> roles;
}
