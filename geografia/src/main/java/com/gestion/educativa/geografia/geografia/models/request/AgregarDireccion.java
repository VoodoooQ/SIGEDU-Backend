package com.gestion.educativa.geografia.geografia.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgregarDireccion {
    @NotBlank
    private String nombre_direccion;

    @NotNull
    private Integer id_comuna;

    @NotBlank
    private String run_usuario_ref;
}
