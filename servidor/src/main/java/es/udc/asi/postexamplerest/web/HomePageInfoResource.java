package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.HomePageInfo;
import es.udc.asi.postexamplerest.model.repository.HomePageInfoDao;
import es.udc.asi.postexamplerest.model.service.CitasService;
import es.udc.asi.postexamplerest.model.service.HomePageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class HomePageInfoResource {

  @Autowired
  private HomePageInfoService homePageInfoService;

  @Autowired
  private HomePageInfoDao homePageInfoDao;

  @GetMapping("/home")
  public ResponseEntity<HomePageInfo> getHomePageInfo() {
    try {
      HomePageInfo info = homePageInfoService.getHomePageInfo();
      return ResponseEntity.ok(info);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  // actualizar info de la pantalla de inicio (solo para jefes)
  @PreAuthorize("hasAuthority('JEFE')")
  @PostMapping(value = "/update", consumes = {"multipart/form-data"})
  public ResponseEntity<Void> updateHomePageInfo(
          @RequestParam("nombre") String nombre,
          @RequestParam("descripcion") String descripcion,
          @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
    try {
      System.out.println("Nombre recibido: " + nombre);
      System.out.println("Descripción recibida: " + descripcion);

      if (imagen != null && !imagen.isEmpty()) {
        System.out.println("Imagen recibida: " + imagen.getOriginalFilename());
      } else {
        System.out.println("No se recibió ninguna imagen.");
      }

      HomePageInfo homePageInfo = new HomePageInfo();
      homePageInfo.setNombre(nombre);
      homePageInfo.setDescripcion(descripcion);

      homePageInfoService.updateHomePageInfo(homePageInfo, imagen);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}
