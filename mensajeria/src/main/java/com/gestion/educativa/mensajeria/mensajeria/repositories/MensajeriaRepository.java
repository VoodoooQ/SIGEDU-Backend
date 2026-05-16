package com.gestion.educativa.mensajeria.mensajeria.repositories;

import java.util.List;
import com.gestion.educativa.mensajeria.mensajeria.models.entity.Mensajeria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensajeriaRepository extends JpaRepository<Mensajeria, Integer> {

    List<Mensajeria> findByRunReceptorRefOrRunReceptorRefIsNull(String runReceptorRef);

    List<Mensajeria> findByRunEmisorRef(String runEmisorRef);
}
