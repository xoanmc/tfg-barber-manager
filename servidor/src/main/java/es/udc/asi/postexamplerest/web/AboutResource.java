package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.About;
import es.udc.asi.postexamplerest.model.service.AboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/about")
public class AboutResource {

  @Autowired
  private AboutService aboutService;

  // Ruta pública: cualquier usuario (registrado o no) puede acceder
  @GetMapping("/{id}")
  public About getAbout(@PathVariable Long id) {
    return aboutService.getAbout(id);
  }

  // Solo los Jefes pueden actualizar el contenido de Acerca de
  @PreAuthorize("hasAuthority('JEFE')")
  @PostMapping("/update")
  public void updateAbout(@RequestBody About about) {
    aboutService.updateAbout(about);
  }
}
