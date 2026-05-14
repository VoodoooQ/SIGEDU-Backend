package com.gestion.educativa.identidad.identidad.models.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "apoderado")
@PrimaryKeyJoinColumn(name = "run_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"estudiantes"}, callSuper = true)
public class Apoderado extends Usuario {

    @Column(name = "parentesco")
    private String parentesco;

    @OneToMany(mappedBy = "apoderado", fetch = FetchType.LAZY)
    private List<Estudiante> estudiantes = new ArrayList<>();
}
