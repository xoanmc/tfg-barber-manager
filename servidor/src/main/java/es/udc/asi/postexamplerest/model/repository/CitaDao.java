package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Cita;
import java.time.LocalDateTime;
import java.util.List;

public interface CitaDao {
  // Crear una nueva cita, debe devolver la cita creada
  Cita create(Cita cita);

  // Actualizar una cita existente
  void update(Cita cita);

  // Eliminar una cita por su ID
  void delete(Cita cita);

  // Buscar una cita por su ID
  Cita findById(Long id);

  // Cambiar nombres para que coincidan con el servicio
  List<Cita> findCitasByCliente(Long clienteId);  // Cambiar el nombre

  List<Cita> findCitasByBarbero(Long barberoId);  // Cambiar el nombre

  // Buscar citas de un barbero en un rango de fechas
  List<Cita> findCitasByBarberoIdAndFechaHoraBetween(Long barberoId, LocalDateTime inicio, LocalDateTime fin);

  // Buscar citas de un cliente en un rango de fechas
  List<Cita> findCitasByClienteIdAndFechaHoraBetween(Long clienteId, LocalDateTime inicio, LocalDateTime fin);
}
