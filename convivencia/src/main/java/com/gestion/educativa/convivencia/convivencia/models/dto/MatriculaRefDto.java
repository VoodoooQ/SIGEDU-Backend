package com.gestion.educativa.convivencia.convivencia.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaRefDto {

    @JsonProperty("id_matricula")
    private Integer idMatricula;

    @JsonProperty("run_estudiante_ref")
    private String runEstudianteRef;

    @JsonProperty("estado")
    private String estado;
}
