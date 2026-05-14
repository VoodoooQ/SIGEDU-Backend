package com.gestion.educativa.identidad.identidad.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioRequest {

    @Size(max = 100)
    private String pNombreUsuario;

    @Size(max = 100)
    private String osNombreUsuario;

    @Size(max = 100)
    private String pApellidoUsuario;

    @Size(max = 100)
    private String osApellidoUsuario;

    @Email
    private String correoUsuario;

    @Size(max = 20)
    private String telefonoUsuario;

    private Character genero;

    @Size(min = 8)
    private String contrasena;
}
