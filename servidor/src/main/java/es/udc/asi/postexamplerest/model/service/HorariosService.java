package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.Empleado;
import es.udc.asi.postexamplerest.model.domain.Horario;
import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.repository.HorarioDao;
import es.udc.asi.postexamplerest.model.repository.UsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class HorariosService {

    @Autowired
    private HorarioDao horarioDao;

    @Autowired
    private UsuarioDao usuarioDao;

    // Obtener horarios por barbero
    public List<Horario> obtenerHorariosPorBarbero(Long barberoId) {
        return horarioDao.findByBarberoId(barberoId);
    }

    // Crear o actualizar horarios
    public void actualizarHorario(Long barberoId, List<Horario> horariosActualizados) {
        Usuario usuario = usuarioDao.findById(barberoId);
        if (usuario == null || !(usuario instanceof Empleado)) {
            throw new RuntimeException("Barbero no encontrado");
        }
        Empleado barbero = (Empleado) usuario;


        // Eliminar los horarios actuales del barbero
        List<Horario> horariosExistentes = horarioDao.findByBarberoId(barberoId);
        horariosExistentes.forEach(horarioDao::delete);

        // Guardar los nuevos horarios
        horariosActualizados.forEach(horario -> {
            horario.setBarbero(barbero);
            horarioDao.create(horario);
        });
    }

    // Obtener todos los horarios (para disponibilidad general)
    public List<Horario> obtenerTodosLosHorarios() {
        return horarioDao.findAll();
    }
}
