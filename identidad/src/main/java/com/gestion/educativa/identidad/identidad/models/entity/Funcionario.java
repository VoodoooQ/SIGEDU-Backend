package com.gestion.educativa.identidad.identidad.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "funcionario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"usuario", "docente", "inspector", "directivo"})
public class Funcionario {

    @Id
    @Column(name = "run_usuario")
    private String runUsuario;

    @Column(name = "titulo")
    private String titulo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "run_usuario")
    private Usuario usuario;

    @OneToOne(mappedBy = "funcionario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Docente docente;

    @OneToOne(mappedBy = "funcionario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Inspector inspector;

    @OneToOne(mappedBy = "funcionario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Directivo directivo;
}
