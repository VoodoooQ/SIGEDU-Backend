package com.gestion.educativa.geografia.geografia.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class ModificarDireccion {
    @NotBlank
    private String nombre_direccion;

    @NotNull
    private Integer id_comuna;
}
