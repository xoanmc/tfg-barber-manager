package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.Cita;
import es.udc.asi.postexamplerest.model.domain.Servicio;
import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.service.CitasService;
import es.udc.asi.postexamplerest.model.service.dto.CitaDTO;
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

  // obtener citas del cliente autenticado
  @GetMapping("/cliente/mis-citas")
  @PreAuthorize("hasAuthority('CLIENTE')")
  public List<Cita> getMisCitas(Authentication authentication) {
    Usuario cliente = (Usuario) authentication.getPrincipal();
    return citasService.findCitasByCliente(cliente.getId());
  }

  // obtener citas de cliente específico
  @GetMapping("/cliente/{clienteId}")
  @PreAuthorize("hasAuthority('CLIENTE')")
  public List<Cita> getCitasCliente(@PathVariable Long clienteId) {
    return citasService.findCitasByCliente(clienteId);
  }

  @PostMapping("/reservar")
  @PreAuthorize("hasAuthority('CLIENTE')")
  public Cita reservarCita(@RequestBody CitaDTO citaDTO, Authentication authentication) {
    // Obtener el cliente autenticado
    CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
    Usuario cliente = customUserPrincipal.getUsuario();

    // Buscar el barbero y el servicio por ID usando los servicios correspondientes
    Usuario barbero = citasService.findBarberoById(citaDTO.getBarberoId());
    Servicio servicio = citasService.findServicioById(citaDTO.getServicioId());

    // Validar barbero y servicio
    if (barbero == null) {
      throw new IllegalArgumentException("El barbero seleccionado no existe.");
    }
    if (servicio == null) {
      throw new IllegalArgumentException("El servicio seleccionado no existe.");
    }

    // Crear la entidad Cita
    Cita cita = new Cita();
    cita.setCliente(cliente);
    cita.setBarbero(barbero);
    cita.setServicio(servicio);
    cita.setFechaHora(citaDTO.getFechaHora());

    // Reservar la cita
    return citasService.reservarCita(cita, cliente);
  }



  @DeleteMapping("/cancelar/{citaId}")
  @PreAuthorize("hasAuthority('CLIENTE')")
  public void cancelarCita(@PathVariable Long citaId) {
    citasService.cancelarCita(citaId);
  }

  // obtener citas del barbero autenticado
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

  // confirmar una cita (por el barbero)
  @PostMapping("/confirmar/{citaId}")
  @PreAuthorize("hasAuthority('EMPLEADO')")
  public void confirmarCita(@PathVariable Long citaId) {
    citasService.confirmarCita(citaId);
  }

  @PostMapping("/rechazar/{citaId}")
  @PreAuthorize("hasAuthority('EMPLEADO')")
  public void rechazarCita(@PathVariable Long citaId) {
    citasService.rechazarCita(citaId);
  }

  @PutMapping("/modificar/{citaId}")
  @PreAuthorize("hasAuthority('EMPLEADO')")
  public Cita modificarCita(@PathVariable Long citaId, @RequestBody Cita citaModificada) {
    return citasService.modificarCita(citaId, citaModificada);
  }
}
