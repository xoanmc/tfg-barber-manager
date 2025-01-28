package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Cita;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface CitaDao {

  Cita create(Cita cita);

  void update(Cita cita);

  void delete(Cita cita);

  Cita findById(Long id);

  List<Cita> findCitasByCliente(Long clienteId);

  List<Cita> findCitasByBarbero(Long barberoId);

  List<Cita> findCitasByBarberoIdAndFechaAndHoraBetween(Long barberoId, LocalDate fecha, LocalTime inicio, LocalTime fin);

  List<Cita> findCitasByClienteIdAndFechaHoraBetween(Long clienteId, LocalDateTime inicio, LocalDateTime fin);

  List<Cita> findCitasByBarberoIdAndFecha(Long barberoId, LocalDate fecha);
}
