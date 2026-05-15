package com.gestion.educativa.identidad.identidad.services;

import java.util.List;
import java.util.Optional;
import com.gestion.educativa.identidad.identidad.models.dto.UsuarioDto;
import com.gestion.educativa.identidad.identidad.models.entity.Apoderado;
import com.gestion.educativa.identidad.identidad.models.entity.Estudiante;
import com.gestion.educativa.identidad.identidad.models.entity.Usuario;
import com.gestion.educativa.identidad.identidad.models.request.CrearUsuarioRequest;
import com.gestion.educativa.identidad.identidad.repositories.ApoderadoRepository;
import com.gestion.educativa.identidad.identidad.repositories.EstudianteRepository;
import com.gestion.educativa.identidad.identidad.repositories.RolRepository;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRepository;
import com.gestion.educativa.identidad.identidad.repositories.UsuarioRolRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private UsuarioRolRepository usuarioRolRepository;
    @Mock
    private ApoderadoRepository apoderadoRepository;
    @Mock
    private EstudianteRepository estudianteRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void limpiarContextoAntes() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void limpiarContextoDespues() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void crearUsuarioFuncionarioNoPuedeCrear() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "87654321",
                        "token",
                        List.of(new SimpleGrantedAuthority("FUNCIONARIO"))
                )
        );

        CrearUsuarioRequest solicitud = new CrearUsuarioRequest(
                "12345678",
                '5',
                "Juan",
                null,
                "Perez",
                null,
                "juan@test.cl",
                "987654321",
                'M',
                "ClaveSegura123",
                "DIRECTIVO",
                "Director General",
                null
        );

        assertThrows(AccessDeniedException.class, () -> usuarioService.crearUsuario(solicitud));
    }

    @Test
    void obtenerUsuarioEstudianteRetornaVistaMinima() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "11111111",
                        "token",
                        List.of(new SimpleGrantedAuthority("ESTUDIANTE"))
                )
        );

        Usuario estudiante = new Usuario();
        estudiante.setRunUsuario("11111111");
        estudiante.setDvrunUsuario('1');
        estudiante.setPNombreUsuario("Max");
        estudiante.setOsNombreUsuario("Andres");
        estudiante.setPApellidoUsuario("Diaz");
        estudiante.setOsApellidoUsuario("Cortes");
        estudiante.setCorreoUsuario("max@test.cl");
        estudiante.setTelefonoUsuario("999999999");
        estudiante.setGenero('M');

        when(usuarioRepository.findById("11111111")).thenReturn(Optional.of(estudiante));

        UsuarioDto dto = usuarioService.obtenerUsuario("11111111", '1');

        assertEquals("11111111", dto.getRunUsuario());
        assertEquals("Max", dto.getPNombreUsuario());
        assertEquals("Diaz", dto.getPApellidoUsuario());
        assertNull(dto.getOsNombreUsuario());
        assertNull(dto.getOsApellidoUsuario());
        assertNull(dto.getCorreoUsuario());
        assertNull(dto.getTelefonoUsuario());
        assertNull(dto.getGenero());
    }

    @Test
    void obtenerUsuarioApoderadoDeEstudianteAsociadoRetornaVistaParcial() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "22222222",
                        "token",
                        List.of(new SimpleGrantedAuthority("APODERADO"))
                )
        );

        Usuario apoderado = new Usuario();
        apoderado.setRunUsuario("22222222");
        apoderado.setDvrunUsuario('K');
        apoderado.setPNombreUsuario("Ana");
        apoderado.setPApellidoUsuario("Lopez");
        apoderado.setCorreoUsuario("ana@test.cl");
        apoderado.setTelefonoUsuario("911111111");
        apoderado.setGenero('F');

        Apoderado entidadApoderado = new Apoderado();
        entidadApoderado.setRunUsuario("22222222");

        Estudiante estudiante = new Estudiante();
        estudiante.setRunUsuario("33333333");
        estudiante.setDvrunUsuario('3');
        estudiante.setPNombreUsuario("Leo");
        estudiante.setPApellidoUsuario("Lopez");
        estudiante.setCorreoUsuario("leo@test.cl");
        estudiante.setTelefonoUsuario("922222222");
        estudiante.setGenero('M');
        estudiante.setApoderado(entidadApoderado);

        when(usuarioRepository.findById("33333333")).thenReturn(Optional.of(estudiante));
        when(estudianteRepository.findById("33333333")).thenReturn(Optional.of(estudiante));

        UsuarioDto dto = usuarioService.obtenerUsuario("33333333", '3');

        assertEquals("33333333", dto.getRunUsuario());
        assertEquals("Leo", dto.getPNombreUsuario());
        assertNull(dto.getCorreoUsuario());
        assertNull(dto.getTelefonoUsuario());
    }
}
