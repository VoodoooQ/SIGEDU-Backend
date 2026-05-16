package com.gestion.educativa.mensajeria.mensajeria.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import com.gestion.educativa.mensajeria.mensajeria.exceptions.RecursoNoEncontradoException;
import com.gestion.educativa.mensajeria.mensajeria.models.dto.MensajeriaDto;
import com.gestion.educativa.mensajeria.mensajeria.models.dto.UsuarioValidadoDto;
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

    public MensajeriaDto enviar(UsuarioValidadoDto usuario, MensajeriaRequest solicitud) {
        validarAcceso(usuario, "DIRECTIVO", "ADMIN", "INSPECTOR", "DOCENTE");
        if (solicitud.getRunReceptor() == null || solicitud.getRunReceptor().isBlank()) {
            throw new IllegalArgumentException("El runReceptor es obligatorio para mensaje directo");
        }

        Mensajeria mensaje = new Mensajeria();
        mensaje.setAsunto(solicitud.getAsunto());
        mensaje.setContenido(solicitud.getContenido());
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setRunEmisorRef(usuario.getRunUsuario());
        mensaje.setRunReceptorRef(solicitud.getRunReceptor());
        return mapearDto(mensajeriaRepository.save(mensaje));
    }

    public MensajeriaDto enviarMasivo(UsuarioValidadoDto usuario, MensajeMasivoRequest solicitud) {
        validarAcceso(usuario, "DIRECTIVO", "ADMIN");

        Mensajeria mensaje = new Mensajeria();
        mensaje.setAsunto(solicitud.getAsunto());
        mensaje.setContenido(solicitud.getContenido());
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setRunEmisorRef(usuario.getRunUsuario());
        mensaje.setRunReceptorRef(null);
        return mapearDto(mensajeriaRepository.save(mensaje));
    }

    public MensajeriaDto responder(UsuarioValidadoDto usuario, Integer idMensaje, MensajeriaRequest solicitud) {
        validarAcceso(usuario, "APODERADO");

        Mensajeria original = mensajeriaRepository.findById(idMensaje)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mensaje no encontrado"));

        if (original.getRunReceptorRef() == null || !original.getRunReceptorRef().equals(usuario.getRunUsuario())) {
            throw new IllegalArgumentException("Solo puedes responder un mensaje recibido por tu usuario");
        }

        Mensajeria respuesta = new Mensajeria();
        respuesta.setAsunto(solicitud.getAsunto());
        respuesta.setContenido(solicitud.getContenido());
        respuesta.setFechaEnvio(LocalDateTime.now());
        respuesta.setRunEmisorRef(usuario.getRunUsuario());
        respuesta.setRunReceptorRef(original.getRunEmisorRef());
        return mapearDto(mensajeriaRepository.save(respuesta));
    }

    public List<MensajeriaDto> obtenerRecibidos(UsuarioValidadoDto usuario) {
        return mensajeriaRepository.findByRunReceptorRefOrRunReceptorRefIsNull(usuario.getRunUsuario())
                .stream()
                .map(this::mapearDto)
                .toList();
    }

    public List<MensajeriaDto> obtenerEnviados(UsuarioValidadoDto usuario) {
        validarAcceso(usuario, "DIRECTIVO", "ADMIN", "INSPECTOR", "DOCENTE");
        return mensajeriaRepository.findByRunEmisorRef(usuario.getRunUsuario())
                .stream()
                .map(this::mapearDto)
                .toList();
    }

    private void validarAcceso(UsuarioValidadoDto usuario, String... rolesPermitidos) {
        Set<String> rolesUsuario = normalizarRoles(usuario.getRoles());
        for (String rolPermitido : rolesPermitidos) {
            if (rolesUsuario.contains(rolPermitido)) {
                return;
            }
        }
        throw new IllegalArgumentException("Acción no permitida para el rol");
    }

    private Set<String> normalizarRoles(List<String> roles) {
        Set<String> normalizados = new HashSet<>();
        if (roles == null) {
            return normalizados;
        }
        for (String rol : roles) {
            if (rol != null && !rol.isBlank()) {
                normalizados.add(rol.trim().toUpperCase(Locale.ROOT));
            }
        }
        if (normalizados.contains("ADMIN")) {
            normalizados.add("DIRECTIVO");
        }
        return normalizados;
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
