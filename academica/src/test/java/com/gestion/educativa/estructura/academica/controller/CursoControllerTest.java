package com.gestion.educativa.estructura.academica.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.educativa.estructura.academica.models.dto.CursoDto;
import com.gestion.educativa.estructura.academica.models.request.CursoRequest;
import com.gestion.educativa.estructura.academica.services.CursoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CursoController.class)
class CursoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CursoService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void listShouldReturnCourses() throws Exception {
        CursoDto c = new CursoDto();
        c.setId(1L);
        c.setNombre("Química");
        Mockito.when(service.findAll()).thenReturn(List.of(c));

        mvc.perform(get("/api/academica/cursos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Química"));
    }

    @Test
    void createShouldReturnCreated() throws Exception {
        CursoRequest req = new CursoRequest();
        req.setNombre("Física");

        CursoDto created = new CursoDto();
        created.setId(5L);
        created.setNombre("Física");

        Mockito.when(service.create(any(CursoRequest.class))).thenReturn(created);

        mvc.perform(post("/api/academica/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/academica/cursos/5"))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.nombre").value("Física"));
    }

    @Test
    void getShouldReturnCourse() throws Exception {
        CursoDto c = new CursoDto();
        c.setId(2L);
        c.setNombre("Biología");
        Mockito.when(service.findById(2L)).thenReturn(c);

        mvc.perform(get("/api/academica/cursos/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Biología"));
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(service).delete(3L);

        mvc.perform(delete("/api/academica/cursos/3"))
                .andExpect(status().isNoContent());
    }
}
