package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.AboutPageInfo;
import es.udc.asi.postexamplerest.model.service.AboutPageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/about")
public class AboutPageInfoResource {

    @Autowired
    private AboutPageInfoService aboutPageInfoService;

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
    public ResponseEntity<Void> updateAboutPageInfo(
            @RequestParam("descripcion") String descripcion,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            AboutPageInfo aboutPageInfo = new AboutPageInfo();
            aboutPageInfo.setDescripcion(descripcion);

            aboutPageInfoService.updateAboutPageInfo(aboutPageInfo, imagen);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
