package es.udc.asi.postexamplerest.model.domain;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Empleado barbero;

    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public Horario() {}

    public Horario(Empleado barbero, String diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        this.barbero = barbero;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empleado getBarbero() {
        return barbero;
    }

    public void setBarbero(Empleado barbero) {
        this.barbero = barbero;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
}

