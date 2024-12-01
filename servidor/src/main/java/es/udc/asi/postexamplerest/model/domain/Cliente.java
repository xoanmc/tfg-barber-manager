package es.udc.asi.postexamplerest.model.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {

  @Column
  private int citas;

  @Column
  private String primeraCita; // Fecha de la primera cita

  @Column(name = "confirmation_token", unique = true)
  private String confirmationToken;

  @Column
  private boolean activo = false; // Campo específico para validar el registro de clientes

  public Cliente() {
    setAutoridad(AutoridadUsuario.CLIENTE);
  }

  public Cliente(int citas, String primeraCita, boolean activo) {
    this.citas = citas;
    this.primeraCita = primeraCita;
    this.activo = activo;
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

  public String getConfirmationToken() {
    return confirmationToken;
  }

  public void setConfirmationToken(String confirmationToken) {
    this.confirmationToken = confirmationToken;
  }

}
