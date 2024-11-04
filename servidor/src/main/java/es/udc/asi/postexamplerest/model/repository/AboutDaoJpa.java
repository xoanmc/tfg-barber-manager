package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.About;
import es.udc.asi.postexamplerest.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

@Repository
public class AboutDaoJpa extends GenericDaoJpa implements AboutDao {

  @Override
  public void create(About about) {
    entityManager.persist(about);
  }

  @Override
  public void update(About about) {
    entityManager.merge(about);
  }

  @Override
  public void delete(About about) {
    entityManager.remove(about);
  }

  @Override
  public About findById(Long id) {
    return entityManager.find(About.class, id);
  }
}
