package com.gestion.educativa.academica.gestionacademica.models.entity;
import jakarta.persistence.Column;      
import jakarta.persistence.Entity;    
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id;        
import jakarta.persistence.Table;  
import lombok.Data;                  
@Entity
@Table(name = "asignaturas")
@Data
public class Asignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignatura")
    private int id_asignatura;

    @Column(name = "nombre_asignatura")
    private String nombre_asignatura;
    @Column (name ="id_nivel_ref")
    private int id_nivel_ref;
    @Column(name="run_docente_ref")
    private String run_docente_ref;
}
