package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.About;

public interface AboutDao {
  void create(About about);

  void update(About about);

  void delete(About about);

  About findById(Long id);
}
