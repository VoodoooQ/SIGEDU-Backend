package com.gestion.educativa.matricula.matricula.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgregarMatricula {
    @NotNull
    private Integer anio_academico;

    @NotBlank
    private String estado;

    @NotBlank
    private String run_estudiante_ref;

    @NotNull
    private Integer id_curso_ref;

    @NotNull
    private Integer id_periodo_ref;
}
