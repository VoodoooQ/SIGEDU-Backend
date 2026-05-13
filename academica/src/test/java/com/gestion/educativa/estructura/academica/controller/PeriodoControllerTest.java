package com.gestion.educativa.estructura.academica.controller;

import com.gestion.educativa.estructura.academica.models.dto.PeriodoDto;
import com.gestion.educativa.estructura.academica.models.request.PeriodoRequest;
import com.gestion.educativa.estructura.academica.services.PeriodoService;
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
class PeriodoControllerTest {

    @Mock
    private PeriodoService service;

    @InjectMocks
    private PeriodoController controller;

    @BeforeEach
    void setUp() {
    }

    @Test
    void listShouldReturnPeriods() {
        PeriodoDto p = new PeriodoDto();
        p.setId(1L);
        p.setNombre("2026-I");
        when(service.findAll()).thenReturn(List.of(p));

        var resp = controller.list();

        assertEquals(200, resp.getStatusCode().value());
        assertEquals(1L, resp.getBody().get(0).getId());
    }

    @Test
    void createShouldReturnCreatedLocation() {
        PeriodoRequest req = new PeriodoRequest();
        req.setNombre("2026-II");

        PeriodoDto created = new PeriodoDto();
        created.setId(5L);
        created.setNombre("2026-II");

        when(service.create(any(PeriodoRequest.class))).thenReturn(created);

        var resp = controller.create(req);

        assertEquals(201, resp.getStatusCode().value());
        assertEquals(URI.create("/api/academica/periodos/5"), resp.getHeaders().getLocation());
        assertEquals(5L, resp.getBody().getId());
    }

    @Test
    void getShouldReturnPeriod() {
        PeriodoDto p = new PeriodoDto();
        p.setId(2L);
        p.setNombre("2025-II");
        when(service.findById(2L)).thenReturn(p);

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
