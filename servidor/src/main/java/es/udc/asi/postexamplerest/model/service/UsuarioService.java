package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.Cliente;
import es.udc.asi.postexamplerest.model.domain.Empleado;
import es.udc.asi.postexamplerest.model.domain.Jefe;
import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.exception.NotFoundException;
import es.udc.asi.postexamplerest.model.exception.OperationNotAllowed;
import es.udc.asi.postexamplerest.model.exception.UserLoginExistsException;
import es.udc.asi.postexamplerest.model.repository.UsuarioDao;
import es.udc.asi.postexamplerest.model.service.dto.AccountDTO;
import es.udc.asi.postexamplerest.model.service.dto.EmpleadoListaDTO;
import es.udc.asi.postexamplerest.model.service.dto.ClienteListaDTO;
import es.udc.asi.postexamplerest.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UsuarioService {

  @Autowired
  private UsuarioDao usuarioDAO;

  @Value("${properties.upload.path}")
  private String uploadPath;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // Método para subir imagen de perfil
  /*@Transactional(readOnly = false)
  public String uploadProfileImage(Long userId, MultipartFile file) throws NotFoundException, IOException {
    Usuario usuario = usuarioDAO.findById(userId);
    if (usuario == null) {
      throw new NotFoundException(userId.toString(), Usuario.class);
    }

    // Guardar la imagen en el servidor
    String fileName = file.getOriginalFilename();
    Path filePath = Paths.get(uploadPath, fileName);
    Files.copy(file.getInputStream(), filePath);

    // Actualizar la URL de la imagen en el usuario
    usuario.setProfileImageUrl(filePath.toString());
    usuarioDAO.update(usuario); // Actualizar el usuario con la nueva imagen

    return usuario.getProfileImageUrl();
  }*/

  // Método para obtener la URL de la imagen de perfil
  public String getProfileImageUrl(Long userId) throws NotFoundException {
    Usuario usuario = usuarioDAO.findById(userId);
    if (usuario == null) {
      throw new NotFoundException(userId.toString(), Usuario.class);
    }
    return usuario.getProfileImageUrl();
  }

  // Métodos para encontrar y gestionar clientes y empleados
  @PreAuthorize("hasAuthority('JEFE')")
  public List<ClienteListaDTO> findAllClientes() {
    return usuarioDAO.findAllClientes().stream()
      .map(cliente -> new ClienteListaDTO(cliente))
      .collect(Collectors.toList());
  }

  @PreAuthorize("hasAuthority('JEFE')")
  public List<EmpleadoListaDTO> findAllEmpleados() {
    return usuarioDAO.findAllEmpleados().stream()
      .map(empleado -> new EmpleadoListaDTO(empleado))
      .collect(Collectors.toList());
  }

  @PreAuthorize("hasAuthority('CLIENTE')")
  public List<EmpleadoListaDTO> findAllBarberos() {
    return usuarioDAO.findAllBarberos().stream()
      .map(empleado -> new EmpleadoListaDTO(empleado))
      .collect(Collectors.toList());
  }

  // Método para buscar un usuario por ID
  public AccountDTO findById(Long id) throws NotFoundException {
    Usuario usuario = usuarioDAO.findById(id);
    if (usuario == null) {
      throw new NotFoundException(id.toString(), Usuario.class);
    }
    return new AccountDTO(usuario);
  }

  // Registrar Cliente con la edad, citas y primera cita
  @Transactional(readOnly = false)
  public void registerCliente(String nombre, String apellido, String telefono, int edad, String login, String password, int citas, String primeraCita) throws UserLoginExistsException {
    if (usuarioDAO.findByLogin(login) != null) {
      throw new UserLoginExistsException(login);
    }

    Cliente cliente = new Cliente();
    String encryptedPassword = passwordEncoder.encode(password);

    cliente.setNombre(nombre);
    cliente.setApellido(apellido);
    cliente.setTelefono(telefono);
    cliente.setEdad(edad);
    cliente.setLogin(login);
    cliente.setPassword(encryptedPassword);
    cliente.setCitas(citas); // Número total de citas del cliente
    cliente.setPrimeraCita(primeraCita); // Fecha de la primera cita

    usuarioDAO.create(cliente);
  }

  // Registrar Empleado con los campos edad, salario, contrato, horario y descripción
  @Transactional(readOnly = false)
  public void registerEmpleado(String nombre, String apellido, String telefono, int edad, String puesto, String login, String password, double salario, String contrato, String horario, String descripcion) throws UserLoginExistsException {
    if (usuarioDAO.findByLogin(login) != null) {
      throw new UserLoginExistsException(login);
    }

    Empleado empleado = new Empleado();
    String encryptedPassword = passwordEncoder.encode(password);

    empleado.setNombre(nombre);
    empleado.setApellido(apellido);
    empleado.setTelefono(telefono);
    empleado.setEdad(edad);
    empleado.setPuesto(puesto);
    empleado.setLogin(login);
    empleado.setPassword(encryptedPassword);
    empleado.setSalario(salario);
    empleado.setContrato(contrato);
    empleado.setHorario(horario);  // Nuevo campo horario
    empleado.setDescripcion(descripcion);  // Nuevo campo descripción

    usuarioDAO.create(empleado);
  }

  // Registrar Jefe
  @Transactional(readOnly = false)
  public void registerJefe(String nombre, String apellido, String telefono, String login, String password) throws UserLoginExistsException {
    if (usuarioDAO.findByLogin(login) != null) {
      throw new UserLoginExistsException(login);
    }

    Jefe jefe = new Jefe();
    String encryptedPassword = passwordEncoder.encode(password);

    jefe.setNombre(nombre);
    jefe.setApellido(apellido);
    jefe.setTelefono(telefono);
    jefe.setLogin(login);
    jefe.setPassword(encryptedPassword);

    usuarioDAO.create(jefe);
  }

  // Obtener información del usuario actual con su autoridad
  public AccountDTO getCurrentUserWithAuthority() {
    String currentUserLogin = SecurityUtils.getCurrentUserLogin();
    if (currentUserLogin != null) {
      return new AccountDTO(usuarioDAO.findByLogin(currentUserLogin));
    }
    return null;
  }

  // Eliminar usuario por ID
  @PreAuthorize("hasAuthority('JEFE')")
  @Transactional(readOnly = false)
  public void deleteById(Long id) throws NotFoundException, OperationNotAllowed {
    Usuario theUser = usuarioDAO.findById(id);
    if (theUser == null) {
      throw new NotFoundException(id.toString(), Usuario.class);
    }
    AccountDTO currentUser = getCurrentUserWithAuthority();
    if (currentUser.getId().equals(theUser.getId())) {
      throw new OperationNotAllowed("El usuario no puede eliminarse a sí mismo");
    }
    usuarioDAO.delete(theUser);
  }
}
