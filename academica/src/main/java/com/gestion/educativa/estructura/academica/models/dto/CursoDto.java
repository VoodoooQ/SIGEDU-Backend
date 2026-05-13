package com.gestion.educativa.estructura.academica.models.dto;

public class CursoDto {

	private Long id;
	private String nombre;
	private String descripcion;
	private Long nivelId;
	private Long periodoId;
	private Long salaId;
	private boolean activo;

	public CursoDto() {
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

	public Long getNivelId() {
		return nivelId;
	}

	public void setNivelId(Long nivelId) {
		this.nivelId = nivelId;
	}

	public Long getPeriodoId() {
		return periodoId;
	}

	public void setPeriodoId(Long periodoId) {
		this.periodoId = periodoId;
	}

	public Long getSalaId() {
		return salaId;
	}

	public void setSalaId(Long salaId) {
		this.salaId = salaId;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}

