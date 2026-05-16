package com.gestion.educativa.matricula.matricula.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class AgregarAntecedenteAcademico {
    @NotBlank    
    private String run_estudiante_ref;
    @NotBlank
    private String colegio_procedencia;
    @NotBlank
    private Float promedio_general;
}
