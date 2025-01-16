package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.Peinado;
import es.udc.asi.postexamplerest.model.repository.PeinadoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeinadoService {

    @Autowired
    private PeinadoDao peinadoDao;

    public List<Peinado> obtenerTodosLosPeinados() {
        return peinadoDao.findAll();
    }


    public List<Peinado> obtenerPeinadosEnTendencia() {
        return peinadoDao.findByTendenciaTrue();
    }

    public Peinado guardarPeinado(Peinado peinado) {
        return peinadoDao.save(peinado);
    }

    public void toggleTendencia(Long id) {
        Peinado peinado = peinadoDao.findById(id).orElseThrow(() -> new RuntimeException("Peinado no encontrado"));
        peinado.setTendencia(!peinado.isTendencia());
        peinadoDao.save(peinado);
    }

}
