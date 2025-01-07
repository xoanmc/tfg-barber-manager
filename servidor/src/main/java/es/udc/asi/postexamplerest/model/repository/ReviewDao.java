package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Review;
import java.util.List;

public interface ReviewDao {
    List<Review> findByBarberoId(Long barberoId);
    Review create(Review review);
    void delete(Long reviewId);  // Nuevo método para eliminar reseña
}
