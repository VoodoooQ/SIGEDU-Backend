package com.gestion.educativa.identidad.identidad.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "Inicio")
public class InicioController {

    @Operation(summary = "Health check")
    @GetMapping("/")
    public String redirigirASwagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
