package es.udc.asi.postexamplerest.model.service.dto;

import es.udc.asi.postexamplerest.model.domain.Cliente;

import java.time.LocalDate;

public class ClienteListaDTO {

    private Long id;
    private String email;
    private String login;
    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDate fechaNacimiento;
    private int citas;
    private String primeraCita;
    private boolean activo;

    public ClienteListaDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.login = cliente.getLogin();
        this.email = cliente.getEmail();
        this.nombre = cliente.getNombre();
        this.apellido = cliente.getApellido();
        this.telefono = cliente.getTelefono();
        this.fechaNacimiento = cliente.getFechaNacimiento();
        this.citas = cliente.getCitas();
        this.primeraCita = cliente.getPrimeraCita();
        this.activo = cliente.isActivo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getCitas() {
        return citas;
    }

    public void setCitas(int citas) {
        this.citas = citas;
    }

    public String getPrimeraCita() {
        return primeraCita;
    }

    public void setPrimeraCita(String primeraCita) {
        this.primeraCita = primeraCita;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
