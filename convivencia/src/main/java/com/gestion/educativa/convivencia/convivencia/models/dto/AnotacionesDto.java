package com.gestion.educativa.convivencia.convivencia.models.dto;

import java.time.LocalDate;

public class AnotacionesDto {

	private Long id;

	private Long estudianteId;

	private LocalDate fecha;

	private String tipo;

	private String descripcion;

	private String registradoPor;

	public AnotacionesDto() {
	}

	public AnotacionesDto(Long id, Long estudianteId, LocalDate fecha, String tipo, String descripcion, String registradoPor) {
		this.id = id;
		this.estudianteId = estudianteId;
		this.fecha = fecha;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.registradoPor = registradoPor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
