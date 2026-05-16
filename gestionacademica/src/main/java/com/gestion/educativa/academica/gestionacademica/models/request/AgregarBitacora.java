package com.gestion.educativa.academica.gestionacademica.models.request;
import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgregarBitacora {
    @NotBlank
    private Date fecha;
    @NotBlank
    private String contenido_visto;
    @NotBlank
    private String observaciones;
    @NotBlank
    private int id_asignatura;
    @NotBlank
    private String run_docente_ref;
}
