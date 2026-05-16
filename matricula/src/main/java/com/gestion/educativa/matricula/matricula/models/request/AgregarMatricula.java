package com.gestion.educativa.matricula.matricula.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgregarMatricula {
    @NotBlank
    private int anio_academico;
    @NotBlank
    private String estado;
    @NotBlank
    private String run_estudiante_ref;
    @NotBlank
    private int id_curso_ref;
    @NotBlank
    private int id_periodo_ref;
}
