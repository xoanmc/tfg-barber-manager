package es.udc.asi.postexamplerest.model.service.dto;

import es.udc.asi.postexamplerest.model.domain.Empleado;

public class EmpleadoListaDTO {

  private Long id;
  private String nombre;
  private String apellido;
  private String telefono;
  private String puesto;
  private int edad;
  private double salario;
  private String contrato;
  private String login;
  private String password;

  public EmpleadoListaDTO(Empleado empleado) {
    this.id = empleado.getId();
    this.nombre = empleado.getNombre();
    this.apellido = empleado.getApellido();
    this.telefono = empleado.getTelefono();
    this.puesto = empleado.getPuesto();
    this.edad = empleado.getEdad();
    this.salario = empleado.getSalario();
    this.contrato = empleado.getContrato();
    this.login = empleado.getLogin();
    this.password = empleado.getPassword();
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

  public int getEdad() {
    return edad;
  }

  public void setEdad(int edad) {
    this.edad = edad;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
