package com.gestion.educativa.matricula.matricula.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import com.gestion.educativa.matricula.matricula.models.dto.UsuarioDTO;

import jakarta.servlet.http.HttpServletRequest;
@Service
public class ValidarUsuarioService {
    @Autowired
    private WebClient identidadWebClient;
    public boolean validarUsuario(String run_usuario) {
        try {
            String rutLimpio = run_usuario.replace(".", "").replace("-", "").trim();
            String run = rutLimpio.substring(0, rutLimpio.length() - 1);
            String dv = rutLimpio.substring(rutLimpio.length() - 1);
          
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes(); 
            String tokenHeader = "";
          
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                tokenHeader = request.getHeader("Authorization");
            }

            UsuarioDTO usuario = identidadWebClient.get()
                    .uri("/api/usuarios/" + run + "/" + dv)
                    .header("Authorization", tokenHeader) 
                    .retrieve()
                    .bodyToMono(UsuarioDTO.class)
                    .block();
                    return usuario != null;

        }catch (Exception e) {
            return false;
        }
    }
}
