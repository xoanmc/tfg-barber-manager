package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.EstructuraFacial;
import es.udc.asi.postexamplerest.model.domain.Peinado;
import es.udc.asi.postexamplerest.model.service.PeinadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/peinados")
public class PeinadoResource {

    @Autowired
    private PeinadoService peinadoService;

    @Value("${properties.upload.path}")
    private String uploadBasePath;

    @GetMapping
    @PreAuthorize("hasAuthority('JEFE')")
    public List<Peinado> obtenerTodosLosPeinados() {
        return peinadoService.obtenerTodosLosPeinados();
    }

    @GetMapping("/tendencias")
    public List<Peinado> obtenerPeinadosEnTendencia() {
        return peinadoService.obtenerPeinadosEnTendencia();
    }

    @GetMapping("/estructuras/{estructuraFacial}")
    public List<Peinado> obtenerPeinadosPorEstructura(@PathVariable String estructuraFacial) {
        try {
            EstructuraFacial estructura = EstructuraFacial.valueOf(estructuraFacial.toUpperCase());
            return peinadoService.obtenerPeinadosPorEstructura(estructura);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estructura facial no válida", e);
        }
    }


    @PostMapping("/guardar")
    @PreAuthorize("hasAuthority('JEFE')")
    public ResponseEntity<String> guardarPeinado(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcionTendencias") String descripcionTendencias,
            @RequestParam("estructurasFaciales") String estructurasFaciales,
            @RequestParam("imagen") MultipartFile imagen) {
        try {
            peinadoService.guardarPeinado(nombre, descripcionTendencias, estructurasFaciales, imagen);
            return ResponseEntity.ok("Peinado guardado con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> getPeinadoImage(@PathVariable String fileName) {
        System.out.println("Invocando getPeinadoImage con fileName: " + fileName);
        try {
            String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
            String uploadDir = uploadBasePath + "/peinados-images/";
            File file = new File(uploadDir + decodedFileName);

            if (!file.exists()) {
                System.out.println("File not found: " + file.getAbsolutePath());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Resource resource = new UrlResource(file.toURI());
            String contentType = Files.probeContentType(file.toPath());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasAuthority('JEFE')")
    @PutMapping("/{id}/toggle-tendencia")
    public ResponseEntity<Void> toggleTendencia(@PathVariable Long id) {
        peinadoService.toggleTendencia(id);
        return ResponseEntity.ok().build();
    }
}
