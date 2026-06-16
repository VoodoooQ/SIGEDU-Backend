package com.gestion.educativa.convivencia.convivencia.models.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "anotaciones")
public class Anotaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "run_estudiante_ref", nullable = false)
    private String runEstudianteRef;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "run_autor_ref")
    private String runAutorRef;

    public Anotaciones() {
    }

    public Anotaciones(String runEstudianteRef, LocalDate fecha, String tipo, String descripcion, String runAutorRef) {
        this.runEstudianteRef = runEstudianteRef;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.runAutorRef = runAutorRef;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRunEstudianteRef() { return runEstudianteRef; }
    public void setRunEstudianteRef(String runEstudianteRef) { this.runEstudianteRef = runEstudianteRef; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getRunAutorRef() { return runAutorRef; }
    public void setRunAutorRef(String runAutorRef) { this.runAutorRef = runAutorRef; }
}