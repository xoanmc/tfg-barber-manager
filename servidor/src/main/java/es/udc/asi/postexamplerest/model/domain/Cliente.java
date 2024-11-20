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

  public Cliente() {
    setAutoridad(AutoridadUsuario.CLIENTE);
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
