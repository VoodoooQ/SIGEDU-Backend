package com.gestion.educativa.academica.gestionacademica.models.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Agregar_Modificar_Asignatura {
    @NotBlank
    private String nombre_asignatura;

    @NotNull
    private Integer id_nivel_ref;

    @NotBlank
    private String run_docente_ref;
}
