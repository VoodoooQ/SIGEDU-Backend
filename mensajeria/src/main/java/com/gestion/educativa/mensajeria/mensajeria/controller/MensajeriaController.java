package com.gestion.educativa.mensajeria.mensajeria.controller;

import java.util.List;
import com.gestion.educativa.mensajeria.mensajeria.models.dto.MensajeriaDto;
import com.gestion.educativa.mensajeria.mensajeria.models.dto.UsuarioValidadoDto;
import com.gestion.educativa.mensajeria.mensajeria.models.request.MensajeMasivoRequest;
import com.gestion.educativa.mensajeria.mensajeria.models.request.MensajeriaRequest;
import com.gestion.educativa.mensajeria.mensajeria.services.MensajeriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mensajeria")
@Tag(name = "Mensajeria")
public class MensajeriaController {

    private final MensajeriaService mensajeriaService;

    @PostMapping("/enviar")
    @Operation(summary = "Enviar mensaje directo")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
    public ResponseEntity<MensajeriaDto> enviar(
            @Valid @RequestBody MensajeriaRequest solicitud,
            HttpServletRequest request) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        solicitud.setRunEmisor(usuario.getRunUsuario());
        MensajeriaDto respuesta = mensajeriaService.enviar(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/masivo")
    @Operation(summary = "Enviar mensaje masivo")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
    public ResponseEntity<MensajeriaDto> enviarMasivo(
            @Valid @RequestBody MensajeMasivoRequest solicitud,
            HttpServletRequest request) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        solicitud.setRunEmisor(usuario.getRunUsuario());
        MensajeriaDto respuesta = mensajeriaService.enviarMasivo(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/responder/{idMensaje}")
    @Operation(summary = "Responder un mensaje recibido")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
    public ResponseEntity<MensajeriaDto> responder(
            @PathVariable Integer idMensaje,
            @Valid @RequestBody MensajeriaRequest solicitud,
            HttpServletRequest request) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        solicitud.setRunEmisor(usuario.getRunUsuario());
        MensajeriaDto respuesta = mensajeriaService.responder(idMensaje, solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/leido/{idMensaje}")
    @Operation(summary = "Marcar mensaje como leido")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", description = "Mensaje no encontrado")
    public ResponseEntity<MensajeriaDto> marcarLeido(
            @PathVariable Integer idMensaje,
            HttpServletRequest request) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        return ResponseEntity.ok(mensajeriaService.marcarLeido(idMensaje, usuario.getRunUsuario()));
    }

    @GetMapping("/recibidos")
    @Operation(summary = "Listar mensajes recibidos")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<MensajeriaDto>> recibidos(HttpServletRequest request) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        return ResponseEntity.ok(mensajeriaService.obtenerRecibidos(usuario.getRunUsuario()));
    }

    @GetMapping("/enviados")
    @Operation(summary = "Listar mensajes enviados")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<MensajeriaDto>> enviados(HttpServletRequest request) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        return ResponseEntity.ok(mensajeriaService.obtenerEnviados(usuario.getRunUsuario()));
    }

    @DeleteMapping("/{idMensaje}")
    @Operation(summary = "Eliminar mensaje")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer idMensaje,
            HttpServletRequest request) {
        UsuarioValidadoDto usuario = (UsuarioValidadoDto) request.getAttribute("usuarioAutenticado");
        mensajeriaService.eliminar(idMensaje, usuario.getRunUsuario());
        return ResponseEntity.noContent().build();
    }
}
