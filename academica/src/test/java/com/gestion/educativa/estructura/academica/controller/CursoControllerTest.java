package com.gestion.educativa.estructura.academica.controller;

import com.gestion.educativa.estructura.academica.models.dto.CursoDto;
import com.gestion.educativa.estructura.academica.models.request.CursoRequest;
import com.gestion.educativa.estructura.academica.services.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CursoControllerTest {

    @Mock
    private CursoService service;

    @InjectMocks
    private CursoController controller;

    @BeforeEach
    void setUp() {
        // controller is injected with mocked service
    }

    @Test
    void listShouldReturnCourses() {
        CursoDto c = new CursoDto();
        c.setId(1L);
        c.setNombre("Química");
        when(service.findAll()).thenReturn(List.of(c));

        var resp = controller.list(new MockHttpServletRequest());

        assertEquals(200, resp.getStatusCode().value());
        assertEquals(1L, resp.getBody().get(0).getId());
    }

    @Test
    void createShouldReturnCreatedLocation() {
        CursoRequest req = new CursoRequest();
        req.setNombre("Física");

        CursoDto created = new CursoDto();
        created.setId(5L);
        created.setNombre("Física");

        when(service.create(any(CursoRequest.class))).thenReturn(created);

        var resp = controller.create(req, new MockHttpServletRequest());

        assertEquals(201, resp.getStatusCode().value());
        assertEquals(URI.create("/api/academica/cursos/5"), resp.getHeaders().getLocation());
        assertEquals(5L, resp.getBody().getId());
    }

    @Test
    void getShouldReturnCourse() {
        CursoDto c = new CursoDto();
        c.setId(2L);
        c.setNombre("Biología");
        when(service.findById(2L)).thenReturn(c);

        var resp = controller.get(2L, new MockHttpServletRequest());

        assertEquals(200, resp.getStatusCode().value());
        assertEquals(2L, resp.getBody().getId());
    }

    @Test
    void deleteShouldReturnNoContent() {
        doNothing().when(service).delete(3L);

        var resp = controller.delete(3L, new MockHttpServletRequest());

        assertEquals(204, resp.getStatusCode().value());
        assertNull(resp.getBody());
    }
}
