package com.gestion.educativa.convivencia.convivencia.models.dto;

import java.time.LocalDate;

public class HojaVidaEstudianteDto {
    private Long id;
    private String runEstudianteRef;
    private String comportamiento;
    private String asistencia;
    private String novedades;
    private LocalDate fechaRegistro;
    private String runAutorRef;

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