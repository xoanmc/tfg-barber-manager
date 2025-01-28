package es.udc.asi.postexamplerest.model.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "barbero_id", nullable = false)
    private Empleado barbero;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    private String comentario;
    private int calificacion;
    private LocalDate fecha;

    @Transient  // campo transitorio, no persistente en la BD
    private String barberoLogin;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Empleado getBarbero() { return barbero; }
    public void setBarbero(Empleado barbero) { this.barbero = barbero; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getBarberoLogin() { return barberoLogin; }
    public void setBarberoLogin(String barberoLogin) { this.barberoLogin = barberoLogin; }
}
