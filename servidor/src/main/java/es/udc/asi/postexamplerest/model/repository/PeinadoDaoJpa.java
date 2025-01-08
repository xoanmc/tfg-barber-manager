package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Peinado;
import es.udc.asi.postexamplerest.model.repository.PeinadoDao;
import es.udc.asi.postexamplerest.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class PeinadoDaoJpa extends GenericDaoJpa implements PeinadoDao {

    @Override
    public List<Peinado> findByTendenciaTrue() {
        return entityManager.createQuery("SELECT p FROM Peinado p WHERE p.tendencia = true", Peinado.class)
                .getResultList();
    }

    @Override
    public Peinado save(Peinado peinado) {
        if (peinado.getId() == null) {
            entityManager.persist(peinado);
            return peinado;
        } else {
            return entityManager.merge(peinado);
        }
    }
}
