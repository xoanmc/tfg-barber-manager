package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.Servicio;
import es.udc.asi.postexamplerest.model.repository.ServiciosDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ServiciosService {

  @Autowired
  private ServiciosDao serviciosDao;

  public List<Servicio> findAll() {
    return serviciosDao.findAll();
  }

  @Transactional
  public void updateServicios(List<Servicio> servicios) {
    for (Servicio servicio : servicios) {
      if (servicio.getId() == null) {
        serviciosDao.create(servicio);
      } else {
        serviciosDao.update(servicio);
      }
      System.out.println("Servicio guardado: " + servicio.getNombre() + " - Precio: " + servicio.getPrecio());
    }
  }

  @Transactional
  public void deleteServicio(Long id) {
    Servicio servicio = serviciosDao.findById(id);
    if (servicio != null) {
      serviciosDao.delete(servicio);
    }
  }
}
