package com.gestion.educativa.geografia.geografia.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name = "direccion")
@Data
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private int idDireccion;
    @Column(name = "nombre_direccion")
    private String nombre_direccion;
    @Column(name = "id_comuna")
    private int id_comuna;
    //Sacar de sesion via MS0 
    @Column(name = "run_usuario_ref")
    private String run_usuario_ref;

    
}