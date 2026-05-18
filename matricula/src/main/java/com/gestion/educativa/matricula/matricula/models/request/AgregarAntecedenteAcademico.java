package com.gestion.educativa.matricula.matricula.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class AgregarAntecedenteAcademico {
    @NotBlank
    private String run_estudiante_ref;

    @NotBlank
    private String colegio_procedencia;

    @NotNull
    private Float promedio_general;
}
