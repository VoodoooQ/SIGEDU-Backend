package com.gestion.educativa.academica.gestionacademica.models.request;
import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgregarBitacora {
    @NotNull
    private Date fecha;

    @NotBlank
    private String contenido_visto;

    @NotBlank
    private String observaciones;

    @NotNull
    private Integer id_asignatura;
}
