package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.HomePageInfo;
import es.udc.asi.postexamplerest.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class HomePageInfoDaoJpa extends GenericDaoJpa implements HomePageInfoDao {

  @Override
  public HomePageInfo find() {
    TypedQuery<HomePageInfo> query = entityManager.createQuery("SELECT h FROM HomePageInfo h", HomePageInfo.class);

    List<HomePageInfo> result = query.getResultList();

    if (result.isEmpty()) {
      return null;
    }

    return result.get(0);
  }

  @Override
  public void update(HomePageInfo homePageInfo) {
    if (homePageInfo.getId() == null) {
      // Si no existe, persistimos un nuevo objeto
      entityManager.persist(homePageInfo);
    } else {
      // Si ya existe, realizamos un merge para actualizarlo
      entityManager.merge(homePageInfo);
    }
  }
}
