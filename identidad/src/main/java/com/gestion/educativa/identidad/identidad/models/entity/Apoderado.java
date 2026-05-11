package com.gestion.educativa.identidad.identidad.models.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "apoderado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"usuario", "estudiantes"})
public class Apoderado {

    @Id
    @Column(name = "run_usuario")
    private String runUsuario;

    @Column(name = "parentesco")
    private String parentesco;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "run_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "apoderado", fetch = FetchType.LAZY)
    private List<Estudiante> estudiantes = new ArrayList<>();
}
