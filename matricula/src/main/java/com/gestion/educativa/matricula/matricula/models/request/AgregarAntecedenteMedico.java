package com.gestion.educativa.matricula.matricula.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgregarAntecedenteMedico {
    @NotNull
    private Boolean alergico;
    
    private String alergias;

    private String medicacion;

    @NotBlank
    private String prevision_salud;

    @NotBlank
    private String tipo_sangre;

    @NotBlank
    private String run_estudiante_ref;
}
