package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.Promocion;
import es.udc.asi.postexamplerest.model.domain.Servicio;
import es.udc.asi.postexamplerest.model.repository.PromocionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PromocionService {

    @Autowired
    private PromocionDao promocionDao;

    public List<Promocion> findAllPromociones() {
        return promocionDao.findAll();
    }

    public List<Promocion> findPromocionesByServicioAndFecha(Servicio servicio, LocalDate fecha) {
        return promocionDao.findByServicioAndFecha(servicio, fecha);
    }

    @Transactional
    public Promocion savePromocion(Promocion promocion) {
        if (promocion.getFechaInicio().isAfter(promocion.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        if (promocion.getPorcentajeDescuento() < 0 || promocion.getPorcentajeDescuento() > 100) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100.");
        }

        if (promocionDao.hasOverlap(promocion)) {
            throw new IllegalArgumentException("Ya existe una promoción activa que solapa con las fechas indicadas.");
        }

        if (promocion.getId() == null) {
            return promocionDao.create(promocion);
        } else {
            return promocionDao.update(promocion);
        }
    }

    @Transactional
    public void deletePromocion(Long id) {
        Promocion promocion = promocionDao.findById(id);
        if (promocion != null) {
            promocionDao.delete(promocion);
        }
    }

    @Transactional
    public void togglePromocion(Long id, boolean activo) {
        Promocion promocion = promocionDao.findById(id);
        if (promocion != null) {
            promocion.setActivo(activo);
            promocionDao.update(promocion);
        }
    }
}