package com.gestion.educativa.estructura.academica.controller;

import com.gestion.educativa.estructura.academica.models.dto.SalaDto;
import com.gestion.educativa.estructura.academica.models.request.SalaRequest;
import com.gestion.educativa.estructura.academica.services.SalaService;
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
class SalaControllerTest {

    @Mock
    private SalaService service;

    @InjectMocks
    private SalaController controller;

    @BeforeEach
    void setUp() {
    }

    @Test
    void listShouldReturnRooms() {
        SalaDto s = new SalaDto();
        s.setId(1L);
        s.setNombre("A101");
        when(service.findAll()).thenReturn(List.of(s));

        var resp = controller.list();

        assertEquals(200, resp.getStatusCode().value());
        assertEquals(1L, resp.getBody().get(0).getId());
    }

    @Test
    void createShouldReturnCreatedLocation() {
        SalaRequest req = new SalaRequest();
        req.setNombre("B202");

        SalaDto created = new SalaDto();
        created.setId(5L);
        created.setNombre("B202");

        when(service.create(any(SalaRequest.class))).thenReturn(created);

        var resp = controller.create(req);

        assertEquals(201, resp.getStatusCode().value());
        assertEquals(URI.create("/api/academica/salas/5"), resp.getHeaders().getLocation());
        assertEquals(5L, resp.getBody().getId());
    }

    @Test
    void getShouldReturnRoom() {
        SalaDto s = new SalaDto();
        s.setId(2L);
        s.setNombre("C303");
        when(service.findById(2L)).thenReturn(s);

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
