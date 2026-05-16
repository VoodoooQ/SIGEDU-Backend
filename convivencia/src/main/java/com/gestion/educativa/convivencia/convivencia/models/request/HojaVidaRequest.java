package com.gestion.educativa.convivencia.convivencia.models.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class HojaVidaRequest {

	@NotNull(message = "El estudiante es requerido")
	private Long estudianteId;

	private String comportamiento;

	private String asistencia;

	private String novedades;

	private LocalDate fechaRegistro;

	private String registradoPor;

	public HojaVidaRequest() {
	}

	public HojaVidaRequest(Long estudianteId, String comportamiento, String asistencia, String novedades, LocalDate fechaRegistro, String registradoPor) {
		this.estudianteId = estudianteId;
		this.comportamiento = comportamiento;
		this.asistencia = asistencia;
		this.novedades = novedades;
		this.fechaRegistro = fechaRegistro;
		this.registradoPor = registradoPor;
	}

	public Long getEstudianteId() {
		return estudianteId;
	}

	public void setEstudianteId(Long estudianteId) {
		this.estudianteId = estudianteId;
	}

	public String getComportamiento() {
		return comportamiento;
	}

	public void setComportamiento(String comportamiento) {
		this.comportamiento = comportamiento;
	}

	public String getAsistencia() {
		return asistencia;
	}

	public void setAsistencia(String asistencia) {
		this.asistencia = asistencia;
	}

	public String getNovedades() {
		return novedades;
	}

	public void setNovedades(String novedades) {
		this.novedades = novedades;
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getRegistradoPor() {
		return registradoPor;
	}

	public void setRegistradoPor(String registradoPor) {
		this.registradoPor = registradoPor;
	}
}
