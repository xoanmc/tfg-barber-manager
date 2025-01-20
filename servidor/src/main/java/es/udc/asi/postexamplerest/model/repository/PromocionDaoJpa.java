package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Promocion;
import es.udc.asi.postexamplerest.model.domain.Servicio;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
public class PromocionDaoJpa implements PromocionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Promocion> findAll() {
        return entityManager.createQuery("SELECT p FROM Promocion p", Promocion.class)
                .getResultList();
    }

    @Override
    public Promocion create(Promocion promocion) {
        entityManager.persist(promocion);
        return promocion;
    }

    @Override
    public Promocion update(Promocion promocion) {
        return entityManager.merge(promocion);
    }

    @Override
    public void delete(Promocion promocion) {
        entityManager.remove(promocion);
    }

    @Override
    public Promocion findById(Long id) {
        return entityManager.find(Promocion.class, id);
    }

    @Override
    public List<Promocion> findByServicioAndFecha(Servicio servicio, LocalDate fecha) {
        String jpql = "SELECT p FROM Promocion p WHERE p.servicio = :servicio " +
                "AND p.activo = true " +
                "AND :fecha BETWEEN p.fechaInicio AND p.fechaFin";
        return entityManager.createQuery(jpql, Promocion.class)
                .setParameter("servicio", servicio)
                .setParameter("fecha", fecha)
                .getResultList();
    }

    @Override
    public boolean hasOverlap(Promocion promocion) {
        List<Promocion> result = entityManager.createQuery(
                        "SELECT p FROM Promocion p WHERE p.servicio = :servicio AND p.activo = true AND (p.fechaInicio <= :fechaFin AND p.fechaFin >= :fechaInicio) AND p.id != :id",
                        Promocion.class
                ).setParameter("servicio", promocion.getServicio())
                .setParameter("fechaInicio", promocion.getFechaInicio())
                .setParameter("fechaFin", promocion.getFechaFin())
                .setParameter("id", promocion.getId() != null ? promocion.getId() : -1L)
                .getResultList();

        return !result.isEmpty();
    }
}
