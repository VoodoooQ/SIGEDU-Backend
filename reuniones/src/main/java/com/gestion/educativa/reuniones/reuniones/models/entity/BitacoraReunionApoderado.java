package com.gestion.educativa.reuniones.reuniones.models.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "bitacora_reunion_apoderado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BitacoraReunionApoderado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora_reunion_apoderado")
    private Long idBitacoraReunionApoderado;

    @Column(name = "fecha_reunion", nullable = false)
    private LocalDate fechaReunion;

    @Column(name = "hora_reunion", nullable = false)
    private LocalTime horaReunion;

    @Column(name = "run_apoderado", nullable = false, length = 12)
    private String runApoderado;

    @Column(name = "lugar", nullable = false, length = 150)
    private String lugar;

    @Column(name = "tema", nullable = false, length = 200)
    private String tema;

    @Column(name = "observaciones", length = 500)
    private String observaciones;
}