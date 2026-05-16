package com.gestion.educativa.convivencia.convivencia.models.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "anotaciones")
public class Anotaciones {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long estudianteId;

	@Column(nullable = false)
	private LocalDate fecha;

	@Column(nullable = false)
	private String tipo;

	@Column(nullable = false, length = 1000)
	private String descripcion;

	private String registradoPor;

	public Anotaciones() {
	}

	public Anotaciones(Long estudianteId, LocalDate fecha, String tipo, String descripcion, String registradoPor) {
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
