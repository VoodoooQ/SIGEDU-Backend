package com.gestion.educativa.identidad.identidad.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApoderadoDto {

    private String runUsuario;
    private String pNombreUsuario;
    private String pApellidoUsuario;
    private String correoUsuario;
    private String telefonoUsuario;
    private String parentesco;
}
