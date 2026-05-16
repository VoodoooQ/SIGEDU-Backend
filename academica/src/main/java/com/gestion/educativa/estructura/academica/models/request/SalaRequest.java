package com.gestion.educativa.estructura.academica.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class SalaRequest {

	@NotBlank(message = "El nombre de la sala es requerido")
	private String nombre;

	private String descripcion;

	@Positive(message = "La capacidad debe ser mayor a 0")
	private int capacidad;

	private Boolean activo;

	public SalaRequest() {
	}

	public SalaRequest(String nombre, String descripcion, int capacidad, Boolean activo) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.capacidad = capacidad;
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

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}
