package com.gestion.educativa.matricula.matricula.services;

import org.springframework.stereotype.Service;

@Service
public class ValidarUsuarioService {
    public boolean validarUsuario(String run_usuario) {
        // TODO: validación delegada al SecurityInterceptor via token
        return true;
    }
}