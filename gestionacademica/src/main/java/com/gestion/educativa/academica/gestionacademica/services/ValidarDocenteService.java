package com.gestion.educativa.academica.gestionacademica.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import com.gestion.educativa.academica.gestionacademica.models.dto.DocenteDTO;

import jakarta.servlet.http.HttpServletRequest;
@Service
public class ValidarDocenteService {
    @Autowired
    private WebClient identidadWebClient;
    public boolean validarDocente(String run_docente) {
        try {
            String rutLimpio = run_docente.replace(".", "").replace("-", "").trim();
            String run = rutLimpio.substring(0, rutLimpio.length() - 1);
            String dv = rutLimpio.substring(rutLimpio.length() - 1);
          
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes(); 
            String tokenHeader = "";
          
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                tokenHeader = request.getHeader("Authorization");
            }

            DocenteDTO usuario = identidadWebClient.get()
                    .uri("/api/usuarios/" + run + "/" + dv)
                    .header("Authorization", tokenHeader) 
                    .retrieve()
                    .bodyToMono(DocenteDTO.class)
                    .block();
                    return usuario != null;

        }catch (Exception e) {
            return false;
        }
    }
}
