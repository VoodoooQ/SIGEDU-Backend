package com.gestion.educativa.matricula.matricula.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgregarAntecedenteMedico {
    @NotBlank    
    private boolean alergico;
    
    private String alergias;

    private String medicacion;
    @NotBlank    
    private String prevision_salud;
    @NotBlank    
    private String tipo_sangre;
    @NotBlank    
    private String run_estudiante_ref;
}
