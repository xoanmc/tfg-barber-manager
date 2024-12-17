package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Servicio;
import java.util.List;

public interface ServiciosDao {

  void create(Servicio servicio);

  void update(Servicio servicio);

  void delete(Servicio servicio);

  Servicio findById(Long id);

  List<Servicio> findAll();
}
