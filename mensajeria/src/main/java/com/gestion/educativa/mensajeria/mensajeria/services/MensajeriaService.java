package com.gestion.educativa.mensajeria.mensajeria.services;

import java.time.LocalDateTime;
import java.util.List;
import com.gestion.educativa.mensajeria.mensajeria.exceptions.RecursoNoEncontradoException;
import com.gestion.educativa.mensajeria.mensajeria.models.dto.MensajeriaDto;
import com.gestion.educativa.mensajeria.mensajeria.models.entity.Mensajeria;
import com.gestion.educativa.mensajeria.mensajeria.models.request.MensajeMasivoRequest;
import com.gestion.educativa.mensajeria.mensajeria.models.request.MensajeriaRequest;
import com.gestion.educativa.mensajeria.mensajeria.repositories.MensajeriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MensajeriaService {

    private final MensajeriaRepository mensajeriaRepository;

    public MensajeriaDto enviar(MensajeriaRequest solicitud) {
        if (solicitud.getRunReceptor() == null || solicitud.getRunReceptor().isBlank()) {
            throw new IllegalArgumentException("El runReceptor es obligatorio para mensaje directo");
        }

        Mensajeria mensaje = new Mensajeria();
        mensaje.setAsunto(solicitud.getAsunto());
        mensaje.setContenido(solicitud.getContenido());
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setRunEmisorRef(solicitud.getRunEmisor());
        mensaje.setRunReceptorRef(solicitud.getRunReceptor());
        return mapearDto(mensajeriaRepository.save(mensaje));
    }

    public MensajeriaDto enviarMasivo(MensajeMasivoRequest solicitud) {
        Mensajeria mensaje = new Mensajeria();
        mensaje.setAsunto(solicitud.getAsunto());
        mensaje.setContenido(solicitud.getContenido());
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setRunEmisorRef(solicitud.getRunEmisor());
        mensaje.setRunReceptorRef(null);
        return mapearDto(mensajeriaRepository.save(mensaje));
    }

    public MensajeriaDto responder(Integer idMensaje, MensajeriaRequest solicitud) {
        Mensajeria original = mensajeriaRepository.findById(idMensaje)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mensaje no encontrado"));

        if (original.getRunReceptorRef() == null || !original.getRunReceptorRef().equals(solicitud.getRunEmisor())) {
            throw new IllegalArgumentException("Solo puedes responder un mensaje recibido por tu usuario");
        }

        Mensajeria respuesta = new Mensajeria();
        respuesta.setAsunto(solicitud.getAsunto());
        respuesta.setContenido(solicitud.getContenido());
        respuesta.setFechaEnvio(LocalDateTime.now());
        respuesta.setRunEmisorRef(solicitud.getRunEmisor());
        respuesta.setRunReceptorRef(original.getRunEmisorRef());
        return mapearDto(mensajeriaRepository.save(respuesta));
    }

    public List<MensajeriaDto> obtenerRecibidos(String runUsuario) {
        if (runUsuario == null || runUsuario.isBlank()) {
            throw new IllegalArgumentException("El runUsuario es obligatorio");
        }

        return mensajeriaRepository.findByRunReceptorRefOrRunReceptorRefIsNull(runUsuario)
                .stream()
                .map(this::mapearDto)
                .toList();
    }

    public List<MensajeriaDto> obtenerEnviados(String runUsuario) {
        if (runUsuario == null || runUsuario.isBlank()) {
            throw new IllegalArgumentException("El runUsuario es obligatorio");
        }

        return mensajeriaRepository.findByRunEmisorRef(runUsuario)
                .stream()
                .map(this::mapearDto)
                .toList();
    }

    private MensajeriaDto mapearDto(Mensajeria mensaje) {
        return new MensajeriaDto(
                mensaje.getIdMensaje(),
                mensaje.getAsunto(),
                mensaje.getContenido(),
                mensaje.getFechaEnvio(),
                mensaje.getRunEmisorRef(),
                mensaje.getRunReceptorRef()
        );
    }
}
