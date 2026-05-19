package com.gestion.educativa.academica.gestionacademica.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class AgregarObjetivo {
    @NotBlank
    private String codigo;

    @NotBlank
    private String descripcion;

    @NotNull
    private Integer id_asignatura;
}
