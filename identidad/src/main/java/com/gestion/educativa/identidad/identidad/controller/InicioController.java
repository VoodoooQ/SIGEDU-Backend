package com.gestion.educativa.identidad.identidad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @GetMapping("/")
    public String redirigirASwagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
