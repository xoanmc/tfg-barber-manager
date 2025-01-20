package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Promocion;
import es.udc.asi.postexamplerest.model.domain.Servicio;
import java.time.LocalDate;
import java.util.List;

public interface PromocionDao {

    Promocion create(Promocion promocion);

    Promocion update(Promocion promocion);

    void delete(Promocion promocion);

    Promocion findById(Long id);

    List<Promocion> findByServicioAndFecha(Servicio servicio, LocalDate fecha);

    public boolean hasOverlap(Promocion promocion);

    List<Promocion> findAll();
}
