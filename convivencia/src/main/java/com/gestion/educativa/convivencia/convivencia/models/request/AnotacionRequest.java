package com.gestion.educativa.convivencia.convivencia.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AnotacionRequest {

	@NotNull(message = "El estudiante es requerido")
	private Long estudianteId;

	@NotNull(message = "La fecha es requerida")
	private LocalDate fecha;

	@NotBlank(message = "El tipo de anotación es requerido")
	private String tipo;

	@NotBlank(message = "La descripción es requerida")
	private String descripcion;

	private String registradoPor;

	public AnotacionRequest() {
	}

	public AnotacionRequest(Long estudianteId, LocalDate fecha, String tipo, String descripcion, String registradoPor) {
		this.estudianteId = estudianteId;
		this.fecha = fecha;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.registradoPor = registradoPor;
	}

	public Long getEstudianteId() {
		return estudianteId;
	}

	public void setEstudianteId(Long estudianteId) {
		this.estudianteId = estudianteId;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getRegistradoPor() {
		return registradoPor;
	}

	public void setRegistradoPor(String registradoPor) {
		this.registradoPor = registradoPor;
	}
}
