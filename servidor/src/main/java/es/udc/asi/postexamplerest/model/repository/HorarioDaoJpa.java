package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Horario;
import es.udc.asi.postexamplerest.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class HorarioDaoJpa extends GenericDaoJpa implements HorarioDao {

    @Override
    public void create(Horario horario) {
        entityManager.persist(horario);
    }

    @Override
    public void update(Horario horario) {
        entityManager.merge(horario);
    }

    @Override
    public void delete(Horario horario) {
        if (entityManager.contains(horario)) {
            entityManager.remove(horario);
        } else {
            entityManager.remove(entityManager.merge(horario));
        }
    }

    @Override
    public Horario findById(Long id) {
        return entityManager.find(Horario.class, id);
    }

    @Override
    public List<Horario> findByBarberoId(Long barberoId) {
        TypedQuery<Horario> query = entityManager.createQuery(
                "SELECT h FROM Horario h WHERE h.barbero.id = :barberoId", Horario.class);
        query.setParameter("barberoId", barberoId);
        return query.getResultList();
    }

    @Override
    public List<Horario> findAll() {
        return entityManager.createQuery("FROM Horario h ORDER BY h.barbero.nombre, h.diaSemana", Horario.class)
                .getResultList();
    }
}
