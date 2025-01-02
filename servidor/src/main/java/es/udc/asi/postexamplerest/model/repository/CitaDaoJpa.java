package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Cita;
import es.udc.asi.postexamplerest.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class CitaDaoJpa extends GenericDaoJpa implements CitaDao {

  @Override
  public Cita create(Cita cita) {
    entityManager.persist(cita);
    return cita;
  }

  @Override
  public void update(Cita cita) {
    entityManager.merge(cita);
  }

  @Override
  public void delete(Cita cita) {
    entityManager.remove(cita);
  }

  @Override
  public Cita findById(Long id) {
    return entityManager.find(Cita.class, id);
  }

  @Override
  public List<Cita> findCitasByCliente(Long clienteId) {
    TypedQuery<Cita> query = entityManager.createQuery("SELECT c FROM Cita c WHERE c.cliente.id = :clienteId", Cita.class);
    query.setParameter("clienteId", clienteId);
    return query.getResultList();
  }

  @Override
  public List<Cita> findCitasByBarbero(Long barberoId) {
    TypedQuery<Cita> query = entityManager.createQuery("SELECT c FROM Cita c WHERE c.barbero.id = :barberoId", Cita.class);
    query.setParameter("barberoId", barberoId);
    return query.getResultList();
  }

  @Override
  public List<Cita> findCitasByBarberoIdAndFechaAndHoraBetween(Long barberoId, LocalDate fecha, LocalTime inicio, LocalTime fin) {
    TypedQuery<Cita> query = entityManager.createQuery(
            "SELECT c FROM Cita c WHERE c.barbero.id = :barberoId AND c.fecha = :fecha AND c.hora BETWEEN :inicio AND :fin",
            Cita.class
    );
    query.setParameter("barberoId", barberoId);
    query.setParameter("fecha", fecha);
    query.setParameter("inicio", inicio);
    query.setParameter("fin", fin);
    return query.getResultList();
  }


  @Override
  public List<Cita> findCitasByClienteIdAndFechaHoraBetween(Long clienteId, LocalDateTime inicio, LocalDateTime fin) {
    TypedQuery<Cita> query = entityManager.createQuery("SELECT c FROM Cita c WHERE c.cliente.id = :clienteId AND c.fechaHora BETWEEN :inicio AND :fin", Cita.class);
    query.setParameter("clienteId", clienteId);
    query.setParameter("inicio", inicio);
    query.setParameter("fin", fin);
    return query.getResultList();
  }

  @Override
  public List<Cita> findCitasByBarberoIdAndFecha(Long barberoId, LocalDate fecha) {
    TypedQuery<Cita> query = entityManager.createQuery(
            "SELECT c FROM Cita c WHERE c.barbero.id = :barberoId AND c.fecha = :fecha", Cita.class);
    query.setParameter("barberoId", barberoId);
    query.setParameter("fecha", fecha);
    return query.getResultList();
  }

}
