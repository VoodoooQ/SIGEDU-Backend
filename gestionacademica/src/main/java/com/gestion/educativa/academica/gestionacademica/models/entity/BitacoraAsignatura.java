package com.gestion.educativa.academica.gestionacademica.models.entity;
import java.sql.Date;

import jakarta.persistence.Column;      
import jakarta.persistence.Entity;    
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id;
import jakarta.persistence.Table;  
import lombok.Data;                  
@Entity
@Table(name = "bitacora_asignaturas")
@Data
public class BitacoraAsignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_bitacora;

    @Column(name = "fecha")
    private Date fecha;
    @Column(name = "contenido_visto")
    private String contenido_visto;
    @Column(name = "observaciones")
    private String observaciones;
   @Column(name = "id_asignatura")
    private int id_asignatura;
    @Column(name="run_docente_ref")
    private String run_docente_ref;

}
