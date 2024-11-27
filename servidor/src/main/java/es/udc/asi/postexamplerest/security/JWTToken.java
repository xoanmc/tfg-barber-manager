package es.udc.asi.postexamplerest.security;

/*
* Estructura para enviar el token en una respuesta HTTP
* */

public class JWTToken {
  private String token;

  public JWTToken(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
