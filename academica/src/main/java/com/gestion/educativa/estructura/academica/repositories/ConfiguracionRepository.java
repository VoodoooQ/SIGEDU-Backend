package com.gestion.educativa.estructura.academica.repositories;

import java.util.Optional;
import com.gestion.educativa.estructura.academica.models.entity.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {
    Optional<Configuracion> findByClave(String clave);
}
