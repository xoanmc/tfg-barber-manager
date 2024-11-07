package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.Cita;
import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.service.CitasService;
import es.udc.asi.postexamplerest.security.CustomUserPrincipal;
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
    Usuario cliente = (Usuario) authentication.getPrincipal();
    return citasService.findCitasByCliente(cliente.getId());
  }

  // Obtener citas de un cliente específico
  @GetMapping("/cliente/{clienteId}")
  @PreAuthorize("hasAuthority('CLIENTE')")
  public List<Cita> getCitasCliente(@PathVariable Long clienteId) {
    return citasService.findCitasByCliente(clienteId);
  }

  // Reservar una nueva cita
  @PostMapping("/reservar")
  //@PreAuthorize("hasAuthority('CLIENTE')")
  public Cita reservarCita(@RequestBody Cita cita) {
    return citasService.reservarCita(cita);
  }

  // Cancelar una cita
  @DeleteMapping("/cancelar/{citaId}")
  @PreAuthorize("hasAuthority('CLIENTE')")
  public void cancelarCita(@PathVariable Long citaId) {
    citasService.cancelarCita(citaId);
  }

  // Obtener citas del barbero autenticado
  @GetMapping("/barbero/mis-citas")
  @PreAuthorize("hasAuthority('EMPLEADO')")
  public List<Cita> getCitasBarbero(Authentication authentication) {
    Object principal = authentication.getPrincipal();
    if (principal instanceof CustomUserPrincipal) {
      CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) principal;
      Usuario barbero = customUserPrincipal.getUsuario();
      return citasService.findCitasByBarbero(barbero.getId());
    } else {
      throw new IllegalStateException("El principal no es de tipo CustomUserPrincipal. Tipo actual: " + principal.getClass().getName());
    }
  }


  // Confirmar una cita (por el barbero)
  @PostMapping("/confirmar/{citaId}")
  @PreAuthorize("hasAuthority('EMPLEADO')")
  public void confirmarCita(@PathVariable Long citaId) {
    citasService.confirmarCita(citaId);
  }

  // Rechazar una cita (por el barbero)
  @PostMapping("/rechazar/{citaId}")
  @PreAuthorize("hasAuthority('EMPLEADO')")
  public void rechazarCita(@PathVariable Long citaId) {
    citasService.rechazarCita(citaId);
  }

  // Modificar una cita (por el barbero)
  @PutMapping("/modificar/{citaId}")
  @PreAuthorize("hasAuthority('EMPLEADO')")
  public Cita modificarCita(@PathVariable Long citaId, @RequestBody Cita citaModificada) {
    return citasService.modificarCita(citaId, citaModificada);
  }
}
