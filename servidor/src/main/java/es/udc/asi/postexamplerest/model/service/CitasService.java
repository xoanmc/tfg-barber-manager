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

  // Obtener todas las citas de un cliente
  public List<Cita> findCitasByCliente(Long clienteId) {
    return citasDao.findCitasByCliente(clienteId);
  }

  // Obtener todas las citas de un barbero
  public List<Cita> findCitasByBarbero(Long barberoId) {
    return citasDao.findCitasByBarbero(barberoId);
  }

  // Reservar una nueva cita
  @Transactional(readOnly = false)
  public Cita reservarCita(Cita cita) {
    // Validar que el barbero existe
    Usuario barbero = usuarioDao.findById(cita.getBarbero().getId());
    if (barbero == null) {
      throw new RuntimeException("El barbero no existe.");
    }

    // Validar que el cliente existe
    Usuario cliente = usuarioDao.findById(cita.getCliente().getId());
    if (cliente == null) {
      throw new RuntimeException("El cliente no existe.");
    }

    // Validar que el servicio existe
    Servicio servicio = servicioDao.findById(cita.getServicio().getId());
    if (servicio == null) {
      throw new RuntimeException("El servicio no existe.");
    }

    // Asignar el objeto barbero, cliente y servicio a la cita
    cita.setBarbero(barbero);
    cita.setCliente(cliente);
    cita.setServicio(servicio);

    // Verificar si el barbero ya tiene una cita en el mismo horario
    List<Cita> citasConflicto = citasDao.findCitasByBarberoIdAndFechaHoraBetween(
            barbero.getId(),
            cita.getFechaHora().minusMinutes(30),
            cita.getFechaHora().plusMinutes(30)
    );

    if (!citasConflicto.isEmpty()) {
      throw new RuntimeException("El barbero ya tiene una cita en ese horario.");
    }

    // Crear la nueva cita y devolverla
    return citasDao.create(cita);
  }


  // Confirmar una cita existente
  @Transactional(readOnly = false)
  public void confirmarCita(Long citaId) {
    Cita cita = citasDao.findById(citaId);

    // Validar si la cita existe
    if (cita == null) {
      throw new RuntimeException("Cita no encontrada.");
    }

    // Cambiar el estado a "Confirmada" y actualizar
    cita.setEstado("Confirmada");
    citasDao.update(cita);
  }

  // Cancelar una cita existente
  @Transactional(readOnly = false)
  public void cancelarCita(Long citaId) {
    Cita cita = citasDao.findById(citaId);

    // Validar si la cita existe
    if (cita == null) {
      throw new RuntimeException("Cita no encontrada.");
    }

    // Eliminar la cita
    citasDao.delete(cita);
  }
}
