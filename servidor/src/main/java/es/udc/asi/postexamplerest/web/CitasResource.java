package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.Cita;
import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.service.CitasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitasResource {

  @Autowired
  private CitasService citasService;

  // Obtener citas del cliente autenticado
  @GetMapping("/cliente/mis-citas")
  @PreAuthorize("hasAuthority('CLIENTE')")
  public List<Cita> getMisCitas(Authentication authentication) {
    Usuario cliente = (Usuario) authentication.getPrincipal(); // Obtener el cliente autenticado
    return citasService.findCitasByCliente(cliente.getId()); // Llamar a la función con el ID del cliente
  }

  // Obtener citas de un cliente
  @GetMapping("/cliente/{clienteId}")
  @PreAuthorize("hasAuthority('CLIENTE')")
  public List<Cita> getCitasCliente(@PathVariable Long clienteId) {
    return citasService.findCitasByCliente(clienteId);
  }

  // Obtener citas de un barbero
  @GetMapping("/barbero/{barberoId}")
  @PreAuthorize("hasAuthority('BARBERO')")
  public List<Cita> getCitasBarbero(@PathVariable Long barberoId) {
    return citasService.findCitasByBarbero(barberoId);
  }

  // Reservar una nueva cita
  @PostMapping("/reservar")
  //@PreAuthorize("hasAuthority('CLIENTE')")
  public Cita reservarCita(@RequestBody Cita cita) {
    return citasService.reservarCita(cita);
  }

  // Confirmar una cita (por el barbero)
  @PostMapping("/confirmar/{citaId}")
  @PreAuthorize("hasAuthority('BARBERO')")
  public void confirmarCita(@PathVariable Long citaId) {
    citasService.confirmarCita(citaId);
  }

  // Cancelar una cita
  @DeleteMapping("/cancelar/{citaId}")
  @PreAuthorize("hasAuthority('CLIENTE')")
  public void cancelarCita(@PathVariable Long citaId) {
    citasService.cancelarCita(citaId);
  }
}
