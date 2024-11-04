package es.udc.asi.postexamplerest.model.service.dto;

import es.udc.asi.postexamplerest.model.domain.Cliente;

public class ClienteListaDTO {

  private Long id;
  private String login;
  private String nombre;
  private String apellido;
  private String telefono;
  private int edad;
  private int citas;
  private String primeraCita; // Fecha de la primera cita en formato String

  public ClienteListaDTO(Cliente cliente) {
    this.id = cliente.getId();
    this.login = cliente.getLogin();
    this.nombre = cliente.getNombre();
    this.apellido = cliente.getApellido();
    this.telefono = cliente.getTelefono();
    this.edad = cliente.getEdad();
    this.citas = cliente.getCitas();
    this.primeraCita = cliente.getPrimeraCita();
  }

  // Getters y setters
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

  public int getEdad() {
    return edad;
  }

  public void setEdad(int edad) {
    this.edad = edad;
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
}
