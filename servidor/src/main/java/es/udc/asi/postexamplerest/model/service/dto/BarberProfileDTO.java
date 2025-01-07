package es.udc.asi.postexamplerest.model.service.dto;

import es.udc.asi.postexamplerest.model.domain.Horario;

import java.util.List;

public class BarberProfileDTO {

    private String nombre;
    private String apellido;
    private String login;
    private String descripcion;
    private String imagenPerfil;  // URL de la imagen
    private List<Horario> horarios;

    public BarberProfileDTO(String nombre, String apellido, String login, String descripcion, String imagenPerfil, List<Horario> horarios) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.login = login;
        this.descripcion = descripcion;
        this.imagenPerfil = imagenPerfil;
        this.horarios = horarios;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }
}
