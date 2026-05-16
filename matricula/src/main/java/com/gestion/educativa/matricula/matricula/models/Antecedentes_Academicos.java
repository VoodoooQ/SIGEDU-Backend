package com.gestion.educativa.matricula.matricula.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name = "antecedentes_academicos")
@Data
public class Antecedentes_Academicos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_antec_acad")
    private int id_antec_acad;
    @Column(name = "run_estudiante_ref")
    private String run_estudiante_ref;
    @Column(name = "colegio_procedencia")
    private String colegio_procedencia;
    @Column(name="promedio_general")
    private Float promedio_general;
}
