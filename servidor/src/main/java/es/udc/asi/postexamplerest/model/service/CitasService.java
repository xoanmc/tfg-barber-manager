package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.Cita;
import es.udc.asi.postexamplerest.model.domain.Horario;
import es.udc.asi.postexamplerest.model.domain.Servicio;
import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.repository.CitaDao;
import es.udc.asi.postexamplerest.model.repository.HorarioDao;
import es.udc.asi.postexamplerest.model.repository.ServiciosDao;
import es.udc.asi.postexamplerest.model.repository.UsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CitasService {

    @Autowired
    private CitaDao citasDao;

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private ServiciosDao servicioDao;

    @Autowired
    private HorarioDao horarioDao;

    public List<Cita> findCitasByCliente(Long clienteId) {
        return citasDao.findCitasByCliente(clienteId);
    }

    public List<Cita> findCitasByBarbero(Long barberoId) {
        System.out.println("Buscando citas para barbero con ID: " + barberoId);
        return citasDao.findCitasByBarbero(barberoId);
    }

    @Transactional(readOnly = true)
    public Usuario findBarberoById(Long barberoId) {
        return usuarioDao.findById(barberoId);
    }

    @Transactional(readOnly = true)
    public Servicio findServicioById(Long servicioId) {
        return servicioDao.findById(servicioId);
    }

    @Transactional(readOnly = false)
    public Cita reservarCita(Cita cita, Usuario clienteAutenticado) {
        if (cita.getBarbero() == null) {
            throw new IllegalArgumentException("Debe seleccionar un barbero para reservar la cita.");
        }

        Usuario barbero = usuarioDao.findById(cita.getBarbero().getId());
        Servicio servicio = servicioDao.findById(cita.getServicio().getId());

        cita.setBarbero(barbero);
        cita.setCliente(clienteAutenticado);
        cita.setServicio(servicio);

        // Validar conflicto de horarios del barbero
        List<Cita> citasConflicto = citasDao.findCitasByBarberoIdAndFecha(barbero.getId(), cita.getFecha());

        // Calcular el rango de tiempo de la nueva cita
        LocalTime inicioNuevaCita = cita.getHora();
        LocalTime finNuevaCita = inicioNuevaCita.plusMinutes(30); // Duración estándar de la cita

        boolean conflicto = citasConflicto.stream().anyMatch(citaExistente -> {
            LocalTime inicioExistente = citaExistente.getHora();
            LocalTime finExistente = inicioExistente.plusMinutes(30); // Duración estándar de las citas existentes

            // Verificar si hay solapamiento entre las citas
            return inicioNuevaCita.isBefore(finExistente) && finNuevaCita.isAfter(inicioExistente);
        });

        if (conflicto) {
            throw new RuntimeException("El barbero ya tiene una cita en ese horario.");
        }

        return citasDao.create(cita);
    }


    @Transactional(readOnly = false)
    public void confirmarCita(Long citaId) {
        Cita cita = citasDao.findById(citaId);
        if (cita == null) throw new RuntimeException("Cita no encontrada.");
        cita.setEstado("Confirmada");
        citasDao.update(cita);
    }

    @Transactional(readOnly = false)
    public void rechazarCita(Long citaId) {
        Cita cita = citasDao.findById(citaId);
        if (cita == null) throw new RuntimeException("Cita no encontrada.");
        cita.setEstado("Rechazada");
        citasDao.update(cita);
    }

    @Transactional(readOnly = false)
    public void cancelarCita(Long citaId) {
        Cita cita = citasDao.findById(citaId);
        if (cita == null) throw new RuntimeException("Cita no encontrada.");
        citasDao.delete(cita);
    }

    @Transactional(readOnly = false)
    public Cita modificarCita(Long citaId, Cita citaModificada) {
        Cita cita = citasDao.findById(citaId);
        if (cita == null) throw new RuntimeException("Cita no encontrada.");
        cita.setFecha(citaModificada.getFecha());
        cita.setHora(citaModificada.getHora());
        citasDao.update(cita);
        return cita;
    }

}
