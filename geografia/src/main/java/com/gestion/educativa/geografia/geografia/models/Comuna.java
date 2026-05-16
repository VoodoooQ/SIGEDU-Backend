package com.gestion.educativa.geografia.geografia.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name = "comuna")
@Data
public class Comuna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comuna")
    private int idComuna;
    @Column(name = "nombre_comuna")
    private String nombre_comuna;
    @ManyToOne
    @JoinColumn(name = "id_ciudad")
    private Ciudad ciudad;
    
}