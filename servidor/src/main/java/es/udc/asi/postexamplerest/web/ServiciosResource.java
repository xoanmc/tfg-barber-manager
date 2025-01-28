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

  @GetMapping
  public List<Servicio> getServicios() {
    return serviciosService.findAll();
  }

  @PreAuthorize("hasAuthority('JEFE')")
  @PostMapping("/update")
  public void updateServicios(@RequestBody List<Servicio> servicios) {
    serviciosService.updateServicios(servicios);
  }

  @PreAuthorize("hasAuthority('JEFE')")
  @DeleteMapping("/{id}")
  public void deleteServicio(@PathVariable Long id) {
    serviciosService.deleteServicio(id);
  }
}
