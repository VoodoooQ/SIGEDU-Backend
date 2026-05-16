package com.gestion.educativa.academica.gestionacademica.models.entity;
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
@Table(name = "objetivos_aprendizaje")
@Data
public class ObjetivosAprendizaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_objetivo;

    @Column(name = "codigo")
    private String codigo;
    @Column(name = "descripcion")
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "id_asignatura")
    private Asignatura asignatura;

}