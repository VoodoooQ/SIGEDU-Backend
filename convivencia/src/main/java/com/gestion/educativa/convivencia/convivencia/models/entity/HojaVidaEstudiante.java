package com.gestion.educativa.convivencia.convivencia.models.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hoja_vida_estudiante")
public class HojaVidaEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "run_estudiante_ref", nullable = false)
    private String runEstudianteRef;

    @Column(length = 1000)
    private String comportamiento;

    @Column(length = 1000)
    private String asistencia;

    @Column(length = 1000)
    private String novedades;

    private LocalDate fechaRegistro;

    @Column(name = "run_autor_ref")
    private String runAutorRef;

    public HojaVidaEstudiante() {
    }

    public HojaVidaEstudiante(String runEstudianteRef, String comportamiento, String asistencia, String novedades, LocalDate fechaRegistro, String runAutorRef) {
        this.runEstudianteRef = runEstudianteRef;
        this.comportamiento = comportamiento;
        this.asistencia = asistencia;
        this.novedades = novedades;
        this.fechaRegistro = fechaRegistro;
        this.runAutorRef = runAutorRef;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRunEstudianteRef() { return runEstudianteRef; }
    public void setRunEstudianteRef(String runEstudianteRef) { this.runEstudianteRef = runEstudianteRef; }
    public String getComportamiento() { return comportamiento; }
    public void setComportamiento(String comportamiento) { this.comportamiento = comportamiento; }
    public String getAsistencia() { return asistencia; }
    public void setAsistencia(String asistencia) { this.asistencia = asistencia; }
    public String getNovedades() { return novedades; }
    public void setNovedades(String novedades) { this.novedades = novedades; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getRunAutorRef() { return runAutorRef; }
    public void setRunAutorRef(String runAutorRef) { this.runAutorRef = runAutorRef; }
}