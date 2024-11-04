package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.HomePageInfo;
import es.udc.asi.postexamplerest.model.service.HomePageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")  // Mantenemos la ruta /api
public class HomePageInfoResource {

  @Autowired
  private HomePageInfoService homePageInfoService;

  // Obtener la información de la pantalla de inicio (disponible para todos)
  @GetMapping("/home")
  public ResponseEntity<HomePageInfo> getHomePageInfo() {
    try {
      HomePageInfo homePageInfo = homePageInfoService.getHomePageInfo();
      if (homePageInfo == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
      return ResponseEntity.ok(homePageInfo);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  // Actualizar la información de la pantalla de inicio (solo para jefes)
  @PreAuthorize("hasAuthority('JEFE')")
  @PostMapping("/update")
  public ResponseEntity<Void> updateHomePageInfo(@RequestBody HomePageInfo homePageInfo) {
    try {
      homePageInfoService.updateHomePageInfo(homePageInfo);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
