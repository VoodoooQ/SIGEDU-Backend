package com.gestion.educativa.convivencia.convivencia.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AnotacionRequest {
    @NotBlank(message = "El run del estudiante es requerido")
    private String runEstudianteRef;
    @NotNull(message = "La fecha es requerida")
    private LocalDate fecha;
    @NotBlank(message = "El tipo de anotacion es requerido")
    private String tipo;
    @NotBlank(message = "La descripcion es requerida")
    private String descripcion;

    public String getRunEstudianteRef() { return runEstudianteRef; }
    public void setRunEstudianteRef(String runEstudianteRef) { this.runEstudianteRef = runEstudianteRef; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}