package es.udc.asi.postexamplerest.model.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorOptions(force = true)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Entity
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "autoridad"  // Esto permite diferenciar los subtipos
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Cliente.class, name = "CLIENTE"),
        @JsonSubTypes.Type(value = Empleado.class, name = "EMPLEADO")
})
public abstract class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
  @SequenceGenerator(name = "user_generator", sequenceName = "user_seq")
  private Long id;

  @Column(unique = true)
  private String login;

  @Column
  private String nombre;

  @Column
  private String apellido;

  @Column
  private String telefono;

  @Column
  private int edad;

  private String password;

  @Enumerated(EnumType.STRING)
  private AutoridadUsuario autoridad;

  @Column
  private String profileImageUrl;


  public Usuario() {
  }

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

  public AutoridadUsuario getAutoridad() {
    return autoridad;
  }

  public void setAutoridad(AutoridadUsuario autoridad) {
    this.autoridad = autoridad;
  }

  public int getEdad() {
    return edad;
  }

  public void setEdad(int edad) {
    this.edad = edad;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }
}
