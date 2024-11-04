package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Servicio;
import java.util.List;

public interface ServiciosDao {

  // Crear un nuevo servicio
  void create(Servicio servicio);

  // Actualizar un servicio existente
  void update(Servicio servicio);

  // Eliminar un servicio
  void delete(Servicio servicio);

  // Buscar un servicio por ID
  Servicio findById(Long id);

  // Obtener todos los servicios
  List<Servicio> findAll();
}
