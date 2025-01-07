package es.udc.asi.postexamplerest.model.repository.jpa;

import es.udc.asi.postexamplerest.model.domain.Review;
import es.udc.asi.postexamplerest.model.repository.ReviewDao;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ReviewDaoJpa implements ReviewDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Review> findByBarberoId(Long barberoId) {
        return entityManager.createQuery("SELECT r FROM Review r WHERE r.barbero.id = :barberoId", Review.class)
                .setParameter("barberoId", barberoId)
                .getResultList();
    }

    @Override
    public Review create(Review review) {
        entityManager.persist(review);
        return review;
    }

    @Override
    public void delete(Long reviewId) {
        Review review = entityManager.find(Review.class, reviewId);
        if (review != null) {
            entityManager.remove(review);
        }
    }
}
