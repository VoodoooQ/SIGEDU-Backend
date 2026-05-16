package com.gestion.educativa.estructura.academica.models.request;

import jakarta.validation.constraints.NotBlank;

public class NivelRequest {

	@NotBlank(message = "El nombre del nivel es requerido")
	private String nombre;

	private String descripcion;

	private Boolean activo;

	public NivelRequest() {
	}

	public NivelRequest(String nombre, String descripcion, Boolean activo) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.activo = activo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}
