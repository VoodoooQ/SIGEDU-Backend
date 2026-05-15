package com.gestion.educativa.identidad.identidad.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "estudiante")
@PrimaryKeyJoinColumn(name = "run_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"apoderado"}, callSuper = true)
public class Estudiante extends Usuario {

    @Column(name = "parentesco", nullable = false, length = 100)
    private String parentesco;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "run_apoderado", nullable = false)
    private Apoderado apoderado;
}
