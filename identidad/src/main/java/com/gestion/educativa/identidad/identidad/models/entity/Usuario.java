package com.gestion.educativa.identidad.identidad.models.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(
        name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_usuario_correo", columnNames = "correo_usuario")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"roles"})
public class Usuario implements UserDetails {

    @Id
    @Column(name = "run_usuario", nullable = false)
    private String runUsuario;

    @Column(name = "dvrun_usuario", nullable = false, length = 1)
    private char dvrunUsuario;

    @Column(name = "p_nombre_usuario", nullable = false, length = 100)
    private String pNombreUsuario;

    @Column(name = "os_nombre_usuario", length = 100)
    private String osNombreUsuario;

    @Column(name = "p_apellido_usuario", nullable = false, length = 100)
    private String pApellidoUsuario;

    @Column(name = "os_apellido_usuario", length = 100)
    private String osApellidoUsuario;

    @Column(name = "correo_usuario", nullable = false, length = 150)
    private String correoUsuario;

    @Column(name = "telefono_usuario", length = 20)
    private String telefonoUsuario;

    @Column(name = "genero", nullable = false, length = 1)
    private char genero;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsuarioRol> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) {
            return List.of();
        }

        Set<String> nombresRol = roles.stream()
                .map(UsuarioRol::getRol)
                .filter(Objects::nonNull)
                .map(Rol::getNombreRol)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(nombreRol -> !nombreRol.isBlank())
                .map(nombreRol -> nombreRol.toUpperCase(Locale.ROOT))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (nombresRol.contains("ADMIN")) {
            // Alias de compatibilidad para el directivo semilla historico.
            nombresRol.add("DIRECTIVO");
        }

        return nombresRol.stream()
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
