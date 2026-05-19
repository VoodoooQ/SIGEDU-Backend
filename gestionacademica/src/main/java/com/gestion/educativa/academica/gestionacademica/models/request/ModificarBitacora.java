package com.gestion.educativa.academica.gestionacademica.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class ModificarBitacora {
    @NotBlank
    private String contenido_visto;

    @NotBlank
    private String observaciones;

    @NotNull
    private Integer id_asignatura;
}
