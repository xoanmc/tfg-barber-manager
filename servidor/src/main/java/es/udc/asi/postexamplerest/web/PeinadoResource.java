package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.Peinado;
import es.udc.asi.postexamplerest.model.service.PeinadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/peinados")
public class PeinadoResource {

    @Autowired
    private PeinadoService peinadoService;

    @GetMapping("/tendencias")
    public List<Peinado> obtenerPeinadosEnTendencia() {
        return peinadoService.obtenerPeinadosEnTendencia();
    }

    @PostMapping("/guardar")
    public ResponseEntity<Peinado> guardarPeinado(@RequestBody Peinado peinado) {
        Peinado nuevoPeinado = peinadoService.guardarPeinado(peinado);
        return ResponseEntity.ok(nuevoPeinado);
    }
}
