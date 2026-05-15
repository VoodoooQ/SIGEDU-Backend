package com.gestion.educativa.reuniones.reuniones.models.dto;

import java.time.LocalDate;
import java.time.LocalTime;
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
public class ReunionGeneralDto {

    private Long idBitacoraReunionGeneral;
    private LocalDate fechaReunion;
    private LocalTime horaReunion;
    private String lugar;
    private String tema;
    private String observaciones;
}