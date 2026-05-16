package com.gestion.educativa.geografia.geografia.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gestion.educativa.geografia.geografia.models.entity.Direccion;
import com.gestion.educativa.geografia.geografia.models.request.AgregarDireccion;
import com.gestion.educativa.geografia.geografia.models.request.ModificarDireccion;
import com.gestion.educativa.geografia.geografia.repositories.DireccionRepository;

@Service
public class DireccionService {
    @Autowired
    private DireccionRepository direccionRepository;
    public Direccion agregarDireccion(AgregarDireccion request, String rut_usuario) {
        Direccion nuevaDireccion = new Direccion();
        nuevaDireccion.setNombre_direccion(request.getNombre_direccion());
        nuevaDireccion.setId_comuna(request.getId_comuna());
        nuevaDireccion.setRun_usuario_ref(rut_usuario);
        return direccionRepository.save(nuevaDireccion);
    }
    public Direccion modificarDireccion(int id_direccion, ModificarDireccion request) {
        Direccion direccionExistente = direccionRepository.findById(id_direccion).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección no encontrada"));
        direccionExistente.setNombre_direccion(request.getNombre_direccion());
        direccionExistente.setId_comuna(request.getId_comuna());
        return direccionRepository.save(direccionExistente);
    }
    public List<Direccion> obtenerDireccionPorRun(String rut_usuario) {
        List<Direccion> direcciones = direccionRepository.findByRunUsuarioRef(rut_usuario);

        if (direcciones.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección para usuario con run " + rut_usuario + " no encontrada");
        }
        return direcciones;
    }
}