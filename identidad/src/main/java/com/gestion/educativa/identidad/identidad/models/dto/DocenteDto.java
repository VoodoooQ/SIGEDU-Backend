package com.gestion.educativa.identidad.identidad.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocenteDto {

    private String runUsuario;
    private String pNombreUsuario;
    private String pApellidoUsuario;
    private String correoUsuario;
    private String especialidad;
}
