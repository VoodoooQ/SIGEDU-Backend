package com.gestion.educativa.matricula.matricula.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgregarAntecedenteApoderado {
    @NotBlank
    private String run_apoderado_ref;
}
