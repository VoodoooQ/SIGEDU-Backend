package com.gestion.educativa.estructura.academica.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConfiguracionRequest {
    @NotBlank
    @Size(max = 100)
    private String clave;

    @NotBlank
    @Size(max = 500)
    private String valor;

    @Size(max = 500)
    private String descripcion;
}
