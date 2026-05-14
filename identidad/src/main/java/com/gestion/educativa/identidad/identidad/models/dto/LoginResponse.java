package com.gestion.educativa.identidad.identidad.models.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String tipo;
    private String runUsuario;
    private List<String> roles;
    private Long expiraEn;
}
