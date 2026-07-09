package com.gestion.educativa.notas.notas.models.entity;

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
@Table(name = "nota")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nota")
    private Long idNota;

    @Column(name = "run_estudiante", nullable = false, length = 12)
    private String runEstudiante;

    @Column(name = "codigo_asignatura", nullable = false, length = 30)
    private String codigoAsignatura;

    @Column(name = "periodo", nullable = false, length = 50)
    private String periodo;

    @Column(name = "tipo_evaluacion", nullable = false, length = 80)
    private String tipoEvaluacion;

    @Column(name = "fecha_evaluacion")
    private LocalDate fechaEvaluacion;

    @Column(name = "ponderacion", nullable = false)
    private Double ponderacion;

    @Column(name = "calificacion", nullable = false)
    private Double calificacion;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "run_docente_ref", length = 12)
    private String runDocenteRef;
}
