package com.gestion.educativa.reuniones.reuniones.models.entity;

import java.time.LocalDate;
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
@Table(name = "acuerdo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Acuerdo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acuerdo")
    private Long idAcuerdo;

    @Column(name = "id_bitacora_reunion_general", nullable = false)
    private Long idBitacoraReunionGeneral;

    @Column(name = "detalle_acuerdo", nullable = false, length = 500)
    private String detalleAcuerdo;

    @Column(name = "responsable", length = 150)
    private String responsable;

    @Column(name = "fecha_compromiso")
    private LocalDate fechaCompromiso;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    @Column(name = "observaciones", length = 500)
    private String observaciones;
}