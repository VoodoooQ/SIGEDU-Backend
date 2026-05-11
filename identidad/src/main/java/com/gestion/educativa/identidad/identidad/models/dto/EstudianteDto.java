package com.gestion.educativa.identidad.identidad.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteDto {

    private String runUsuario;
    private String pNombreUsuario;
    private String pApellidoUsuario;
    private String correoUsuario;
    private String parentesco;
    private String runApoderado;
}
