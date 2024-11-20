package es.udc.asi.postexamplerest.model.service.dto;

import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.domain.Empleado;
import es.udc.asi.postexamplerest.model.domain.Cliente;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class AccountDTO {

  private Long id;
  private String nombre;
  private String apellido;
  private String email; // Nuevo campo para email
  private String telefono;
  @NotEmpty
  private String login;
  @NotEmpty
  @Size(min = 4)
  private String password;
  private String puesto;
  private String autoridad;
  private LocalDate fechaNacimiento; // Reemplaza al campo edad
  private double salario;
  private String contrato;
  private int citas;  // Campo para el número de citas del cliente
  private String primeraCita;  // Campo para la fecha de la primera cita
  private String horario;  // Campo para empleados
  private String descripcion;  // Campo para empleados

  public AccountDTO() {
  }

  public AccountDTO(Usuario usuario) {
    this.id = usuario.getId();
    this.nombre = usuario.getNombre();
    this.apellido = usuario.getApellido();
    this.email = usuario.getEmail(); // Asignar el nuevo campo email
    this.telefono = usuario.getTelefono();
    this.login = usuario.getLogin();

    // Si es un cliente, llenamos campos adicionales para clientes
    if (usuario instanceof Cliente cliente) {
      this.citas = cliente.getCitas();
      this.primeraCita = cliente.getPrimeraCita();
      this.fechaNacimiento = cliente.getFechaNacimiento(); // Campo fechaNacimiento
    }

    // Si es un empleado, llenamos campos adicionales para empleados
    if (usuario instanceof Empleado empleado) {
      this.puesto = empleado.getPuesto();
      this.salario = empleado.getSalario();
      this.contrato = empleado.getContrato();
      this.horario = empleado.getHorario();
      this.descripcion = empleado.getDescripcion();
    }

    this.autoridad = usuario.getAutoridad().name();
  }

  // Getters y Setters para todos los campos

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
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

  public String getPuesto() {
    return puesto;
  }

  public void setPuesto(String puesto) {
    this.puesto = puesto;
  }

  public String getAutoridad() {
    return autoridad;
  }

  public void setAutoridad(String autoridad) {
    this.autoridad = autoridad;
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
