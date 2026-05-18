package com.gestion.educativa.mensajeria.mensajeria.controller;

import java.util.List;
import com.gestion.educativa.mensajeria.mensajeria.models.dto.MensajeriaDto;
import com.gestion.educativa.mensajeria.mensajeria.models.request.MensajeMasivoRequest;
import com.gestion.educativa.mensajeria.mensajeria.models.request.MensajeriaRequest;
import com.gestion.educativa.mensajeria.mensajeria.services.MensajeriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<MensajeriaDto> enviar(@Valid @RequestBody MensajeriaRequest solicitud) {
        MensajeriaDto respuesta = mensajeriaService.enviar(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/masivo")
    @Operation(summary = "Enviar mensaje masivo")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
    public ResponseEntity<MensajeriaDto> enviarMasivo(@Valid @RequestBody MensajeMasivoRequest solicitud) {
        MensajeriaDto respuesta = mensajeriaService.enviarMasivo(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/responder/{idMensaje}")
    @Operation(summary = "Responder un mensaje recibido")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
    public ResponseEntity<MensajeriaDto> responder(
            @PathVariable Integer idMensaje,
            @Valid @RequestBody MensajeriaRequest solicitud) {
        MensajeriaDto respuesta = mensajeriaService.responder(idMensaje, solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping("/recibidos")
    @Operation(summary = "Listar mensajes recibidos")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<MensajeriaDto>> recibidos(@RequestParam String runUsuario) {
        return ResponseEntity.ok(mensajeriaService.obtenerRecibidos(runUsuario));
    }

    @GetMapping("/enviados")
    @Operation(summary = "Listar mensajes enviados")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<MensajeriaDto>> enviados(@RequestParam String runUsuario) {
        return ResponseEntity.ok(mensajeriaService.obtenerEnviados(runUsuario));
    }
}
