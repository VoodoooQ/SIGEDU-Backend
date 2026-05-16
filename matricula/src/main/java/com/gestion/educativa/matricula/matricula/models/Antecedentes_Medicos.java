package com.gestion.educativa.matricula.matricula.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "antecedentes_medicos")
@Data
public class Antecedentes_Medicos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_antec_med")
    private int id_antec_med;
    @Column(name = "alergico")
    private boolean alergico;
    @Column(name="alergias")
    private String alergias;
    @Column(name="medicacion")
    private String medicacion;
    @Column(name="prevision_salud")
    private String prevision_salud;
    @Column(name="tipo_sangre")
    private String tipo_sangre;
    @Column(name="run_estudiante_ref")
    private String run_estudiante_ref;
}
