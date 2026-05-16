package com.gestion.educativa.academica.gestionacademica.models.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Agregar_Modificar_Asignatura {
    @NotBlank
    private String nombre_asignatura;
    @NotBlank
    private int id_nivel_ref;
    @NotBlank
    private String run_docente_ref;
}
