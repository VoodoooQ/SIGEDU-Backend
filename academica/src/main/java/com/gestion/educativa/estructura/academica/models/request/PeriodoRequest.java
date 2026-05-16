package com.gestion.educativa.estructura.academica.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PeriodoRequest {

	@NotBlank(message = "El nombre del período es requerido")
	private String nombre;

	@NotNull(message = "La fecha de inicio es requerida")
	private LocalDate fechaInicio;

	@NotNull(message = "La fecha de fin es requerida")
	private LocalDate fechaFin;

	private Boolean activo;

	public PeriodoRequest() {
	}

	public PeriodoRequest(String nombre, LocalDate fechaInicio, LocalDate fechaFin, Boolean activo) {
		this.nombre = nombre;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.activo = activo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}
