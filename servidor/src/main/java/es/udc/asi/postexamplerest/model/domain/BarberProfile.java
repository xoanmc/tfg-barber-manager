package es.udc.asi.postexamplerest.model.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class BarberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String descripcion;
    private String imagenPerfil; // Nombre del archivo de imagen
    private String login; // Identificador único para URL amigable

    @OneToMany(mappedBy = "barbero", cascade = CascadeType.ALL)
    private List<Horario> horarios; // Horarios del barbero

    public BarberProfile() {
    }

    public BarberProfile(String nombre, String apellido, String descripcion, String login) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.descripcion = descripcion;
        this.login = login;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }
}
