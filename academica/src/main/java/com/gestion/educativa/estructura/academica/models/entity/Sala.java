package com.gestion.educativa.estructura.academica.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "salas")
public class Sala {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nombre;

	@Column(length = 500)
	private String descripcion;

	private int capacidad;

	private boolean activo = true;

	public Sala() {
	}

	public Sala(String nombre, String descripcion, int capacidad, boolean activo) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.capacidad = capacidad;
		this.activo = activo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}
