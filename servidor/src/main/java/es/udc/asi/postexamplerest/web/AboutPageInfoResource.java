package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.AboutPageInfo;
import es.udc.asi.postexamplerest.model.domain.Empleado;
import es.udc.asi.postexamplerest.model.repository.UsuarioDao;
import es.udc.asi.postexamplerest.model.service.AboutPageInfoService;
import es.udc.asi.postexamplerest.model.service.UsuarioService;
import es.udc.asi.postexamplerest.model.service.dto.EmpleadoListaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/about")
public class AboutPageInfoResource {

    @Autowired
    private AboutPageInfoService aboutPageInfoService;

    @Autowired
    private UsuarioDao usuarioDao;

    @GetMapping
    public ResponseEntity<AboutPageInfo> getAboutPageInfo() {
        try {
            AboutPageInfo info = aboutPageInfoService.getAboutPageInfo();
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasAuthority('JEFE')")
    @PostMapping(value = "/update", consumes = {"multipart/form-data"})
    public ResponseEntity<AboutPageInfo> updateAboutPageInfo(
            @RequestParam("descripcion") String descripcion,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            AboutPageInfo aboutPageInfo = aboutPageInfoService.getAboutPageInfo();
            if (aboutPageInfo == null) {
                aboutPageInfo = new AboutPageInfo();
            }
            aboutPageInfo.setDescripcion(descripcion);
            aboutPageInfoService.updateAboutPageInfo(aboutPageInfo, imagen);
            return ResponseEntity.ok(aboutPageInfo);  // Devuelve el objeto actualizado
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/barberos")
    public ResponseEntity<List<EmpleadoListaDTO>> getBarberos() {
        try {
            List<Empleado> barberos = usuarioDao.findAllBarberos();

            List<EmpleadoListaDTO> barberosDTO = barberos.stream()
                    .map(EmpleadoListaDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(barberosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
