package com.gestion.educativa.notas.notas.models.dto;

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
public class NotaDto {

    private Long idNota;
    private String runEstudiante;
    private String codigoAsignatura;
    private String periodo;
    private String tipoEvaluacion;
    private Double ponderacion;
    private Double calificacion;
    private String observaciones;
    private String runDocenteRef;
}