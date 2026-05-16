package com.gestion.educativa.academica.gestionacademica.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class AgregarObjetivo {
    @NotBlank
    private String codigo;
    @NotBlank   
    private String descripcion;
    @NotBlank
    private int id_asignatura;
}
