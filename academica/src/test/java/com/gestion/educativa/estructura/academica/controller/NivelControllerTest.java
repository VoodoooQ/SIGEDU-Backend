package com.gestion.educativa.estructura.academica.controller;

import com.gestion.educativa.estructura.academica.models.dto.NivelDto;
import com.gestion.educativa.estructura.academica.models.request.NivelRequest;
import com.gestion.educativa.estructura.academica.services.NivelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NivelControllerTest {

    @Mock
    private NivelService service;

    @InjectMocks
    private NivelController controller;

    @BeforeEach
    void setUp() {
    }

    @Test
    void listShouldReturnLevels() {
        NivelDto n = new NivelDto();
        n.setId(1L);
        n.setNombre("Primero");
        when(service.findAll()).thenReturn(List.of(n));

        var resp = controller.list();

        assertEquals(200, resp.getStatusCode().value());
        assertEquals(1L, resp.getBody().get(0).getId());
    }

    @Test
    void createShouldReturnCreatedLocation() {
        NivelRequest req = new NivelRequest();
        req.setNombre("Segundo");

        NivelDto created = new NivelDto();
        created.setId(5L);
        created.setNombre("Segundo");

        when(service.create(any(NivelRequest.class))).thenReturn(created);

        var resp = controller.create(req);

        assertEquals(201, resp.getStatusCode().value());
        assertEquals(URI.create("/api/academica/niveles/5"), resp.getHeaders().getLocation());
        assertEquals(5L, resp.getBody().getId());
    }

    @Test
    void getShouldReturnLevel() {
        NivelDto n = new NivelDto();
        n.setId(2L);
        n.setNombre("Tercero");
        when(service.findById(2L)).thenReturn(n);

        var resp = controller.get(2L);

        assertEquals(200, resp.getStatusCode().value());
        assertEquals(2L, resp.getBody().getId());
    }

    @Test
    void deleteShouldReturnNoContent() {
        doNothing().when(service).delete(3L);

        var resp = controller.delete(3L);

        assertEquals(204, resp.getStatusCode().value());
        assertNull(resp.getBody());
    }
}
