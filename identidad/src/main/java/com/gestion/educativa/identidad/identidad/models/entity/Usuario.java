package com.gestion.educativa.identidad.identidad.models.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"roles"})
public class Usuario implements UserDetails {

    @Id
    @Column(name = "run_usuario")
    private String runUsuario;

    @Column(name = "dvrun_usuario")
    private char dvrunUsuario;

    @Column(name = "p_nombre_usuario")
    private String pNombreUsuario;

    @Column(name = "os_nombre_usuario")
    private String osNombreUsuario;

    @Column(name = "p_apellido_usuario")
    private String pApellidoUsuario;

    @Column(name = "os_apellido_usuario")
    private String osApellidoUsuario;

    @Column(name = "correo_usuario")
    private String correoUsuario;

    @Column(name = "telefono_usuario")
    private String telefonoUsuario;

    @Column(name = "genero")
    private char genero;

    @Column(name = "contrasena")
    private String contrasena;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsuarioRol> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .map(UsuarioRol::getRol)
                .filter(Objects::nonNull)
                .map(Rol::getNombreRol)
                .filter(Objects::nonNull)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return runUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
