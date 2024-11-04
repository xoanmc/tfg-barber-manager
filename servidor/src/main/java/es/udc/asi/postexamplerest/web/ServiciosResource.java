package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.Servicio;
import es.udc.asi.postexamplerest.model.service.ServiciosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServiciosResource {

  @Autowired
  private ServiciosService serviciosService;

  // Cualquier persona puede obtener la lista de servicios
  @GetMapping
  public List<Servicio> getServicios() {
    return serviciosService.findAll();
  }

  // Solo los jefes pueden actualizar la lista de servicios
  @PreAuthorize("hasAuthority('JEFE')")
  @PostMapping("/update")
  public void updateServicios(@RequestBody List<Servicio> servicios) {
    serviciosService.updateServicios(servicios);
  }

  // Eliminar un servicio por ID
  @PreAuthorize("hasAuthority('JEFE')")
  @DeleteMapping("/{id}")
  public void deleteServicio(@PathVariable Long id) {
    serviciosService.deleteServicio(id);
  }
}
