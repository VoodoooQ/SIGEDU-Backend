package com.gestion.educativa.identidad.identidad.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "directivo")
@PrimaryKeyJoinColumn(name = "run_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Directivo extends Funcionario {

    @Column(name = "cargo", nullable = false, length = 150)
    private String cargo;
}
