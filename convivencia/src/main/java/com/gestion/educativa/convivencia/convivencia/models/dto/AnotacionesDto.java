package com.gestion.educativa.convivencia.convivencia.models.dto;

import java.time.LocalDate;

public class AnotacionesDto {
    private Long id;
    private String runEstudianteRef;
    private LocalDate fecha;
    private String tipo;
    private String descripcion;
    private String runAutorRef;

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