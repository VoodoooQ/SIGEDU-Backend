package com.gestion.educativa.identidad.identidad.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignarRolRequest {

    @NotBlank
    private String runUsuario;

    @NotNull
    private Integer idRol;
}
