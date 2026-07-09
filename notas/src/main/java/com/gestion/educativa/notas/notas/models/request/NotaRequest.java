package com.gestion.educativa.notas.notas.models.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotaRequest {

    @NotBlank
    private String runEstudiante;

    @NotBlank
    private String codigoAsignatura;

    @NotBlank
    private String periodo;

    @NotBlank
    private String tipoEvaluacion;

    private LocalDate fechaEvaluacion;

    @NotNull
    private Double ponderacion;

    @NotNull
    private Double calificacion;

    private String observaciones;
}
