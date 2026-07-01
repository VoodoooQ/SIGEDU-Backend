package com.gestion.educativa.academica.gestionacademica.services;

import org.springframework.stereotype.Service;

@Service
public class ValidarDocenteService {
    public boolean validarDocente(String run_docente) {
        // TODO: validación delegada al SecurityInterceptor via token
        return true;
    }
}
