package com.gestion.educativa.identidad.identidad.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearUsuarioRequest {

    @NotBlank
    private String runUsuario;

    @NotNull
    private Character dvrunUsuario;

    @NotBlank
    @Size(max = 100)
    private String pNombreUsuario;

    @Size(max = 100)
    private String osNombreUsuario;

    @NotBlank
    @Size(max = 100)
    private String pApellidoUsuario;

    @Size(max = 100)
    private String osApellidoUsuario;

    @NotBlank
    @Email
    private String correoUsuario;

    @Size(max = 20)
    private String telefonoUsuario;

    @NotNull
    private Character genero;

    @NotBlank
    @Size(min = 8)
    private String contrasena;

    @NotBlank
    private String tipoUsuario;

    @Size(max = 150)
    private String campoEspecifico;

    private String runApoderado;
}
