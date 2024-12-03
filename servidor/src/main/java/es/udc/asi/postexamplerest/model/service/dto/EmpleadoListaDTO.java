package es.udc.asi.postexamplerest.model.service.dto;

import es.udc.asi.postexamplerest.model.domain.Empleado;

import java.time.LocalDate;

public class EmpleadoListaDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String puesto;
    private String email;
    private double salario;
    private String contrato;
    private String login;
    private String horario;
    private String descripcion;
    private boolean despedido;

    // Constructor
    public EmpleadoListaDTO(Empleado empleado) {
        this.id = empleado.getId();
        this.nombre = empleado.getNombre();
        this.apellido = empleado.getApellido();
        this.telefono = empleado.getTelefono();
        this.puesto = empleado.getPuesto();
        this.email = empleado.getEmail(); // Fecha de nacimiento
        this.salario = empleado.getSalario();
        this.contrato = empleado.getContrato();
        this.login = empleado.getLogin();
        this.horario = empleado.getHorario();       // Asignar horario
        this.descripcion = empleado.getDescripcion(); // Asignar descripción
        this.despedido = empleado.isDespedido();
    }

    // Getters y setters
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
