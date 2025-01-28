package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.Promocion;
import es.udc.asi.postexamplerest.model.domain.Servicio;
import es.udc.asi.postexamplerest.model.service.PromocionService;
import es.udc.asi.postexamplerest.model.service.dto.PromocionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/promociones")
public class PromocionResource {

    @Autowired
    private PromocionService promocionService;

    @GetMapping
    public List<PromocionDTO> findAllPromociones() {
        return promocionService.findAllPromociones()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping("/{servicioId}/{fecha}")
    public List<Promocion> findPromociones(@PathVariable Long servicioId, @PathVariable LocalDate fecha) {
        return promocionService.findPromocionesByServicioAndFecha(new Servicio(servicioId), fecha);
    }

    @PreAuthorize("hasAuthority('JEFE')")
    @PostMapping
    public PromocionDTO savePromocion(@RequestBody PromocionDTO promocionDTO) {
        Promocion promocion = toEntity(promocionDTO);
        Promocion savedPromocion = promocionService.savePromocion(promocion);
        return toDTO(savedPromocion);
    }

    @PreAuthorize("hasAuthority('JEFE')")
    @PutMapping("/{id}")
    public PromocionDTO updatePromocion(@PathVariable Long id, @RequestBody PromocionDTO promocionDTO) {
        promocionDTO.setId(id);
        Promocion promocion = toEntity(promocionDTO);
        Promocion updatedPromocion = promocionService.savePromocion(promocion);
        return toDTO(updatedPromocion);
    }

    @PreAuthorize("hasAuthority('JEFE')")
    @PutMapping("/{id}/toggle")
    public void togglePromocion(@PathVariable Long id, @RequestParam boolean activo) {
        promocionService.togglePromocion(id, activo);
    }

    @PreAuthorize("hasAuthority('JEFE')")
    @DeleteMapping("/{id}")
    public void deletePromocion(@PathVariable Long id) {
        promocionService.deletePromocion(id);
    }

    private PromocionDTO toDTO(Promocion promocion) {
        PromocionDTO dto = new PromocionDTO();
        dto.setId(promocion.getId());
        dto.setServicioId(promocion.getServicio() != null ? promocion.getServicio().getId() : null);
        dto.setServicioNombre(promocion.getServicio() != null ? promocion.getServicio().getNombre() : "Sin servicio asociado");
        dto.setPorcentajeDescuento(promocion.getPorcentajeDescuento());
        dto.setFechaInicio(promocion.getFechaInicio());
        dto.setFechaFin(promocion.getFechaFin());
        dto.setActivo(promocion.isActivo());
        return dto;
    }

    private Promocion toEntity(PromocionDTO dto) {
        Promocion promocion = new Promocion();
        promocion.setId(dto.getId());
        promocion.setServicio(dto.getServicioId() != null ? new Servicio(dto.getServicioId()) : null);
        promocion.setPorcentajeDescuento(dto.getPorcentajeDescuento());
        promocion.setFechaInicio(dto.getFechaInicio());
        promocion.setFechaFin(dto.getFechaFin());
        promocion.setActivo(dto.isActivo());
        return promocion;
    }
}