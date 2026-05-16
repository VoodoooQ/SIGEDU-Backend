package com.gestion.educativa.geografia.geografia.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ModificarDireccion {
    @NotBlank
    private String nombre_direccion;
    @NotBlank
    private int id_comuna;
}
