package com.gestion.educativa.mensajeria.mensajeria.models.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "mensajeria")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Mensajeria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Integer idMensaje;

    @Column(name = "asunto", nullable = false, length = 255)
    private String asunto;

    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "run_emisor_ref", nullable = false, length = 255)
    private String runEmisorRef;

    @Column(name = "run_receptor_ref", length = 255)
    private String runReceptorRef;

    @Column(name = "leido", nullable = false)
    private Boolean leido = false;
}

