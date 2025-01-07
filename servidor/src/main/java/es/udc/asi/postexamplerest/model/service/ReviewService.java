package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.Empleado;
import es.udc.asi.postexamplerest.model.domain.Review;
import es.udc.asi.postexamplerest.model.repository.ReviewDao;
import es.udc.asi.postexamplerest.model.repository.UsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReviewService {

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private UsuarioDao usuarioDao;

    public List<Review> getReviewsByBarberLogin(String login) {
        Empleado barbero = (Empleado) usuarioDao.findByLogin(login);
        if (barbero == null) {
            throw new RuntimeException("Barbero no encontrado.");
        }
        return reviewDao.findByBarberoId(barbero.getId());
    }

    @Transactional
    public Review addReview(Review review) {
        review.setFecha(LocalDate.now());
        return reviewDao.create(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewDao.delete(reviewId);
    }
}
