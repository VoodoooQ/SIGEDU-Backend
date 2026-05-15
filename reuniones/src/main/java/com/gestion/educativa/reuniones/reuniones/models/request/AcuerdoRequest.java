package com.gestion.educativa.reuniones.reuniones.models.request;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AcuerdoRequest {

    @NotNull
    private Long idBitacoraReunionGeneral;

    @NotBlank
    private String detalleAcuerdo;

    private String responsable;

    private LocalDate fechaCompromiso;

    @NotBlank
    private String estado;

    private String observaciones;
}