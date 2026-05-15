package com.gestion.educativa.notas.notas.services;

import java.util.List;
import java.util.Optional;
import com.gestion.educativa.notas.notas.models.entity.Nota;
import com.gestion.educativa.notas.notas.repositories.NotaRepository;
import org.springframework.stereotype.Service;

@Service
public class NotaService {

    private final NotaRepository notaRepository;

    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public List<Nota> listar() {
        return notaRepository.findAll();
    }

    public Nota guardar(Nota nota) {
        return notaRepository.save(nota);
    }

    public Optional<Nota> buscarPorId(Long idNota) {
        return notaRepository.findById(idNota);
    }

    public void eliminar(Long idNota) {
        notaRepository.deleteById(idNota);
    }
}