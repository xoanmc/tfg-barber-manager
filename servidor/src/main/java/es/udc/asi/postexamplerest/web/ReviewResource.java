package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.Cliente;
import es.udc.asi.postexamplerest.model.domain.Empleado;
import es.udc.asi.postexamplerest.model.domain.Review;
import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.repository.UsuarioDao;
import es.udc.asi.postexamplerest.model.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewResource {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UsuarioDao usuarioDao;

    @GetMapping("/{login}")
    public List<Review> getReviewsByBarberLogin(@PathVariable String login) {
        return reviewService.getReviewsByBarberLogin(login);
    }

    @PostMapping("/add")
    public ResponseEntity<Review> addReview(@RequestBody Review review, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String barberoLogin = review.getBarberoLogin();
        if (barberoLogin == null || barberoLogin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Si no se envía el login del barbero
        }

        Empleado barbero = usuarioDao.findEmpleadoByLogin(barberoLogin);
        if (barbero == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Si el barbero no existe
        }

        Usuario cliente = usuarioDao.findByLogin(principal.getName());
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        review.setBarbero(barbero);
        review.setCliente((Cliente) cliente);
        review.setFecha(LocalDate.now());

        Review createdReview = reviewService.addReview(review);
        return ResponseEntity.ok(createdReview);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
