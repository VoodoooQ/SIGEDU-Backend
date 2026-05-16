package com.gestion.educativa.mensajeria.mensajeria.models.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeriaDto {

    private Integer idMensaje;
    private String asunto;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private String runEmisorRef;
    private String runReceptorRef;
}
