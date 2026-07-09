package com.gestion.educativa.matricula.matricula.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaResumenDto {
    private long total;
    private long presentes;
    private long ausentes;
    private long atrasos;
    private int porcentaje;
}
