package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Peinado;
import java.util.List;
import java.util.Optional;

public interface PeinadoDao {
    List<Peinado> findByTendenciaTrue();
    Peinado save(Peinado peinado);
    Optional<Peinado> findById(Long id);
    List<Peinado> findAll();

}
