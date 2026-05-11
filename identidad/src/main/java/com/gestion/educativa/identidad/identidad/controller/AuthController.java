package com.gestion.educativa.identidad.identidad.controller;

import java.util.Map;
import com.gestion.educativa.identidad.identidad.models.request.LoginRequest;
import com.gestion.educativa.identidad.identidad.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest solicitud) {
        String token = authService.login(solicitud);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
