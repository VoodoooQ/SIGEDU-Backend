package com.gestion.educativa.matricula.matricula.models.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "matriculas")
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_matricula")
    private int id_matricula;

    @Column(name = "anio_academico")
    private int anio_academico;

    @Column(name = "estado")
    private String estado;

    @Column(name = "run_estudiante_ref")
    private String run_estudiante_ref;

    @Column(name = "id_curso_ref")
    private int id_curso_ref;

    @Column(name = "id_periodo_ref")
    private int id_periodo_ref;
}
