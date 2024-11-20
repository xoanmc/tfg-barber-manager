package es.udc.asi.postexamplerest.model.service.dto;

import es.udc.asi.postexamplerest.model.domain.Empleado;

import java.time.LocalDate;

public class EmpleadoListaDTO {

  private Long id;
  private String nombre;
  private String apellido;
  private String telefono;
  private String puesto;
  private LocalDate fechaNacimiento; // Fecha de nacimiento
  private double salario;
  private String contrato;
  private String login;
  private String horario;      // Nuevo campo horario
  private String descripcion;  // Nuevo campo descripción

  // Constructor
  public EmpleadoListaDTO(Empleado empleado) {
    this.id = empleado.getId();
    this.nombre = empleado.getNombre();
    this.apellido = empleado.getApellido();
    this.telefono = empleado.getTelefono();
    this.puesto = empleado.getPuesto();
    this.fechaNacimiento = empleado.getFechaNacimiento(); // Fecha de nacimiento
    this.salario = empleado.getSalario();
    this.contrato = empleado.getContrato();
    this.login = empleado.getLogin();
    this.horario = empleado.getHorario();       // Asignar horario
    this.descripcion = empleado.getDescripcion(); // Asignar descripción
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

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(LocalDate fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public double getSalario() {
    return salario;
  }

  public void setSalario(double salario) {
    this.salario = salario;
  }

  public String getContrato() {
    return contrato;
  }

  public void setContrato(String contrato) {
    this.contrato = contrato;
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
