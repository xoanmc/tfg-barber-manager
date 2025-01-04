package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.AboutPageInfo;
import es.udc.asi.postexamplerest.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class AboutPageInfoDaoJpa extends GenericDaoJpa implements AboutPageInfoDao {

    @Override
    public AboutPageInfo find() {
        TypedQuery<AboutPageInfo> query = entityManager.createQuery("SELECT a FROM AboutPageInfo a", AboutPageInfo.class);
        List<AboutPageInfo> result = query.getResultList();

        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

    @Override
    public void update(AboutPageInfo aboutPageInfo) {
        if (aboutPageInfo.getId() == null) {
            entityManager.persist(aboutPageInfo);
        } else {
            entityManager.merge(aboutPageInfo);
        }
    }
}
