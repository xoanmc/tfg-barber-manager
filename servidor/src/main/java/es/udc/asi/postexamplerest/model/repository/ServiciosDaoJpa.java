package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Servicio;
import es.udc.asi.postexamplerest.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ServiciosDaoJpa extends GenericDaoJpa implements ServiciosDao {

  @Override
  public void create(Servicio servicio) {
    entityManager.persist(servicio);
  }

  @Override
  public void update(Servicio servicio) {
    entityManager.merge(servicio);
  }

  @Override
  public void delete(Servicio servicio) {
    entityManager.remove(servicio);
  }

  @Override
  public Servicio findById(Long id) {
    return entityManager.find(Servicio.class, id);
  }

  @Override
  public List<Servicio> findAll() {
    TypedQuery<Servicio> query = entityManager.createQuery("SELECT s FROM Servicio s", Servicio.class);
    return query.getResultList();
  }
}
