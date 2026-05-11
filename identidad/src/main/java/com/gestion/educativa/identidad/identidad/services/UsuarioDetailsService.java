package com.gestion.educativa.identidad.identidad.services;

import com.gestion.educativa.identidad.identidad.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String runUsuario) throws UsernameNotFoundException {
        return usuarioRepository.findById(runUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + runUsuario));
    }
}
