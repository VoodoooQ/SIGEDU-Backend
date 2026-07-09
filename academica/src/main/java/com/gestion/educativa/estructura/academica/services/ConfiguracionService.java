package com.gestion.educativa.estructura.academica.services;

import java.util.List;
import com.gestion.educativa.estructura.academica.exceptions.ResourceNotFoundException;
import com.gestion.educativa.estructura.academica.models.entity.Configuracion;
import com.gestion.educativa.estructura.academica.models.request.ConfiguracionRequest;
import com.gestion.educativa.estructura.academica.repositories.ConfiguracionRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionService {
    private final ConfiguracionRepository repository;

    public ConfiguracionService(ConfiguracionRepository repository) {
        this.repository = repository;
    }

    public List<Configuracion> findAll() {
        return repository.findAll();
    }

    public Configuracion create(ConfiguracionRequest request) {
        Configuracion config = new Configuracion();
        aplicar(config, request);
        return repository.save(config);
    }

    public Configuracion update(Long id, ConfiguracionRequest request) {
        Configuracion config = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion no encontrada"));
        aplicar(config, request);
        return repository.save(config);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Configuracion no encontrada");
        }
        repository.deleteById(id);
    }

    private void aplicar(Configuracion config, ConfiguracionRequest request) {
        config.setClave(request.getClave());
        config.setValor(request.getValor());
        config.setDescripcion(request.getDescripcion());
    }
}
