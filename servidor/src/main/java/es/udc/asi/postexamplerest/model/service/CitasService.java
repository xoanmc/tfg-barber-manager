package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.Cita;
import es.udc.asi.postexamplerest.model.domain.Servicio;
import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.repository.CitaDao;
import es.udc.asi.postexamplerest.model.repository.ServiciosDao;
import es.udc.asi.postexamplerest.model.repository.UsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CitasService {

  @Autowired
  private CitaDao citasDao;

  @Autowired
  private UsuarioDao usuarioDao;

  @Autowired
  private ServiciosDao servicioDao;

  public List<Cita> findCitasByCliente(Long clienteId) {
    return citasDao.findCitasByCliente(clienteId);
  }

  public List<Cita> findCitasByBarbero(Long barberoId) {
    System.out.println("Buscando citas para barbero con ID: " + barberoId);
    return citasDao.findCitasByBarbero(barberoId);
  }


  @Transactional(readOnly = false)
  public Cita reservarCita(Cita cita, Usuario clienteAutenticado) {
    Usuario barbero = usuarioDao.findById(cita.getBarbero().getId());
    Servicio servicio = servicioDao.findById(cita.getServicio().getId());

    cita.setBarbero(barbero);
    cita.setCliente(clienteAutenticado);
    cita.setServicio(servicio);

    // Validar conflicto de horarios del barbero
    List<Cita> citasConflicto = citasDao.findCitasByBarberoIdAndFechaHoraBetween(
            barbero.getId(),
            cita.getFechaHora().minusMinutes(30),
            cita.getFechaHora().plusMinutes(30)
    );

    if (!citasConflicto.isEmpty()) {
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
    cita.setFechaHora(citaModificada.getFechaHora());
    citasDao.update(cita);
    return cita;
  }
}
