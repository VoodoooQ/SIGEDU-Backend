package com.gestion.educativa.matricula.matricula.models.request;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsistenciaRequest {
    @NotBlank
    private String runEstudianteRef;

    @NotNull
    private LocalDate fecha;

    @NotBlank
    private String estado;

    private String runDocenteRef;

    private Boolean justificada;
}
