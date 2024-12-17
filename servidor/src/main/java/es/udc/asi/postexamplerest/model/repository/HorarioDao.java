package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Horario;

import java.util.List;

public interface HorarioDao {

    void create(Horario horario);

    void update(Horario horario);

    void delete(Horario horario);

    Horario findById(Long id);

    List<Horario> findByBarberoId(Long barberoId);

    List<Horario> findAll();
}
