package com.gestion.educativa.geografia.geografia.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgregarDireccion {
    @NotBlank
    private String nombre_direccion;
    @NotBlank
    private int id_comuna;
    //Sacar de sesion via MS0 
    @NotBlank
    private String run_usuario_ref;
}
