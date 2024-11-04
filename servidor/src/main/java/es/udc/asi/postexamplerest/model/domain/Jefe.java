package es.udc.asi.postexamplerest.model.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("JEFE")
public class Jefe extends Usuario {

  public Jefe() {
    setAutoridad(AutoridadUsuario.JEFE);
  }

}
