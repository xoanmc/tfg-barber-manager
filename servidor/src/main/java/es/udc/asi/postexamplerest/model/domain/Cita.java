package es.udc.asi.postexamplerest.model.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Cita {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Usuario cliente;  // relación con el cliente

  @ManyToOne
  private Usuario barbero;  // relación con el barbero
  private LocalDateTime fechaHora; // fecha y hora de la cita

  @Column(nullable = false)
  private String estado = "Pendiente";  // Confirmada, Pendiente, Cancelada

  @ManyToOne
  private Servicio servicio;  // servicio que se reservó

  public Cita() {}

  public Cita(Usuario cliente, Usuario barbero, LocalDateTime fechaHora, Servicio servicio, String estado) {
    this.cliente = cliente;
    this.barbero = barbero;
    this.fechaHora = fechaHora;
    this.servicio = servicio;
    this.estado = estado;
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

  public LocalDateTime getFechaHora() {
    return fechaHora;
  }

  public void setFechaHora(LocalDateTime fechaHora) {
    this.fechaHora = fechaHora;
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
}
