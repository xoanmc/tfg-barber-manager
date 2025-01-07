package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.Empleado;
import es.udc.asi.postexamplerest.model.domain.Horario;
import es.udc.asi.postexamplerest.model.exception.NotFoundException;
import es.udc.asi.postexamplerest.model.repository.UsuarioDao;
import es.udc.asi.postexamplerest.model.service.UsuarioService;
import es.udc.asi.postexamplerest.model.service.HorariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/barber-profile")
public class BarberProfileResource {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private HorariosService horariosService;

    @GetMapping("/{login}")
    public ResponseEntity<Map<String, Object>> getBarberProfile(@PathVariable String login) {
        Empleado barbero = (Empleado) usuarioDao.findByLogin(login);
        if (barbero == null || !barbero.getPuesto().equalsIgnoreCase("barbero")) {
            return ResponseEntity.notFound().build();
        }

        // Obtener los horarios del barbero
        List<Horario> horarios = horariosService.obtenerHorariosPorBarbero(barbero.getId());

        Map<String, Object> response = Map.of(
                "nombre", barbero.getNombre(),
                "apellido", barbero.getApellido(),
                "descripcion", barbero.getDescripcion(),
                "imagenPerfil", barbero.getProfileImageUrl(),
                "horarios", horarios
        );

        return ResponseEntity.ok(response);
    }
}
