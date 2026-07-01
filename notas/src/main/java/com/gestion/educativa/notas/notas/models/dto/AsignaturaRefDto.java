package com.gestion.educativa.notas.notas.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaRefDto {

    @JsonProperty("id_asignatura")
    private Integer id;

    @JsonProperty("nombre_asignatura")
    private String nombreAsignatura;

    @JsonProperty("run_docente_ref")
    private String runDocenteRef;
}
