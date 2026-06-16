package com.gestion.educativa.convivencia.convivencia.models.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class HojaVidaRequest {
    @NotBlank(message = "El run del estudiante es requerido")
    private String runEstudianteRef;
    private String comportamiento;
    private String asistencia;
    private String novedades;
    private LocalDate fechaRegistro;

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
}