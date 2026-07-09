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

        Mensajeria mensaje = crearMensaje(
                solicitud.getRunEmisor(),
                solicitud.getRunReceptor(),
                solicitud.getAsunto(),
                solicitud.getContenido()
        );
        return mapearDto(mensajeriaRepository.save(mensaje));
    }

    public MensajeriaDto enviarMasivo(MensajeMasivoRequest solicitud) {
        List<String> receptores = solicitud.getRunReceptores() == null
                ? List.of()
                : solicitud.getRunReceptores().stream()
                        .map(this::soloRun)
                        .filter(run -> !run.isBlank())
                        .filter(run -> !run.equals(soloRun(solicitud.getRunEmisor())))
                        .distinct()
                        .toList();

        if (receptores.isEmpty()) {
            Mensajeria mensaje = crearMensaje(
                    solicitud.getRunEmisor(),
                    null,
                    solicitud.getAsunto(),
                    solicitud.getContenido()
            );
            return mapearDto(mensajeriaRepository.save(mensaje));
        }

        List<MensajeriaDto> enviados = receptores.stream()
                .map(runReceptor -> crearMensaje(
                        solicitud.getRunEmisor(),
                        runReceptor,
                        solicitud.getAsunto(),
                        solicitud.getContenido()
                ))
                .map(mensajeriaRepository::save)
                .map(this::mapearDto)
                .toList();

        return enviados.get(0);
    }

    public MensajeriaDto responder(Integer idMensaje, MensajeriaRequest solicitud) {
        Mensajeria original = mensajeriaRepository.findById(idMensaje)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mensaje no encontrado"));

        if (original.getRunReceptorRef() == null || !original.getRunReceptorRef().equals(solicitud.getRunEmisor())) {
            throw new IllegalArgumentException("Solo puedes responder un mensaje recibido por tu usuario");
        }

        Mensajeria respuesta = crearMensaje(
                solicitud.getRunEmisor(),
                original.getRunEmisorRef(),
                solicitud.getAsunto(),
                solicitud.getContenido()
        );
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

    public MensajeriaDto marcarLeido(Integer idMensaje, String runUsuario) {
        Mensajeria mensaje = mensajeriaRepository.findById(idMensaje)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mensaje no encontrado"));

        boolean esReceptor = mensaje.getRunReceptorRef() != null && mensaje.getRunReceptorRef().equals(runUsuario);
        boolean esMasivoLegacy = mensaje.getRunReceptorRef() == null;
        if (!esReceptor && !esMasivoLegacy) {
            throw new IllegalArgumentException("Solo puedes marcar como leido un mensaje recibido por tu usuario");
        }

        mensaje.setLeido(true);
        return mapearDto(mensajeriaRepository.save(mensaje));
    }

    public void eliminar(Integer idMensaje, String runUsuario) {
        Mensajeria mensaje = mensajeriaRepository.findById(idMensaje)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mensaje no encontrado"));

        boolean esEmisor = mensaje.getRunEmisorRef().equals(runUsuario);
        boolean esReceptor = mensaje.getRunReceptorRef() != null && mensaje.getRunReceptorRef().equals(runUsuario);
        if (!esEmisor && !esReceptor) {
            throw new IllegalArgumentException("Solo puedes eliminar mensajes enviados o recibidos por tu usuario");
        }

        mensajeriaRepository.delete(mensaje);
    }

    private Mensajeria crearMensaje(String runEmisor, String runReceptor, String asunto, String contenido) {
        Mensajeria mensaje = new Mensajeria();
        mensaje.setAsunto(asunto);
        mensaje.setContenido(contenido);
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setRunEmisorRef(soloRun(runEmisor));
        mensaje.setRunReceptorRef(runReceptor == null ? null : soloRun(runReceptor));
        mensaje.setLeido(false);
        return mensaje;
    }

    private String soloRun(String valor) {
        return String.valueOf(valor == null ? "" : valor).replaceAll("\\D", "");
    }

    private MensajeriaDto mapearDto(Mensajeria mensaje) {
        return new MensajeriaDto(
                mensaje.getIdMensaje(),
                mensaje.getAsunto(),
                mensaje.getContenido(),
                mensaje.getFechaEnvio(),
                mensaje.getRunEmisorRef(),
                mensaje.getRunReceptorRef(),
                Boolean.TRUE.equals(mensaje.getLeido())
        );
    }
}
