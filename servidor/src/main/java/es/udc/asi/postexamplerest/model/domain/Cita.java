package es.udc.asi.postexamplerest.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario cliente;  // relación con el cliente

    @ManyToOne
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    private Usuario barbero;  // relación con el barbero

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime hora;

    @Column(nullable = false)
    private String estado = "Pendiente";  // Confirmada, Pendiente, Cancelada

    @ManyToOne
    private Servicio servicio;

    @Column(length = 500)
    private String preferencias;

    public Cita() {
    }

    public Cita(Long id, Usuario cliente, Usuario barbero, LocalDate fecha, LocalTime hora, String estado, Servicio servicio) {
        this.id = id;
        this.cliente = cliente;
        this.barbero = barbero;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.servicio = servicio;
        this.preferencias = preferencias;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Usuario getBarbero() {
        return barbero;
    }

    public void setBarbero(Usuario barbero) {
        this.barbero = barbero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public String getPreferencias() { return preferencias; }

    public void setPreferencias(String preferencias) { this.preferencias = preferencias; }
}
