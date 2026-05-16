package com.gestion.educativa.matricula.matricula.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name = "antecedentes_apoderado")
@Data
public class Antecedentes_Apoderado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_antec_ap")
    private int id_antec_ap;
    @Column(name="run_apoderado_ref")
    private String run_apoderado_ref;

}
