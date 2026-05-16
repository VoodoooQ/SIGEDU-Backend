package com.gestion.educativa.academica.gestionacademica.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ModificarBitacora {
    @NotBlank
    private String contenido_visto;
    @NotBlank
    private String observaciones;
    @NotBlank
    private int id_asignatura;
}
