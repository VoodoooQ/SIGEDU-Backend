package com.gestion.educativa.identidad.identidad.models.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {

    private String runUsuario;
    private char dvrunUsuario;
    private String pNombreUsuario;
    private String osNombreUsuario;
    private String pApellidoUsuario;
    private String osApellidoUsuario;
    private String correoUsuario;
    private String telefonoUsuario;
    private char genero;
    private List<String> roles;
}
