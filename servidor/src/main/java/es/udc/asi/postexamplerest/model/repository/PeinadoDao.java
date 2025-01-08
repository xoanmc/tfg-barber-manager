package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Peinado;
import java.util.List;

public interface PeinadoDao {
    List<Peinado> findByTendenciaTrue();
    Peinado save(Peinado peinado);
}
