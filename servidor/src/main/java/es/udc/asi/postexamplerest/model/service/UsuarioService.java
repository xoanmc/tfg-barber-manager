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
import es.udc.asi.postexamplerest.model.service.dto.ClienteListaDTO;
import es.udc.asi.postexamplerest.model.service.dto.EmpleadoListaDTO;
import es.udc.asi.postexamplerest.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

  @Transactional(readOnly = false)
  public Cliente registerCliente(String nombre, String apellido, String telefono, LocalDate fechaNacimiento,
                                 String email, String login, String password, int citas, String primeraCita)
          throws UserLoginExistsException {
    if (usuarioDAO.findByLogin(login) != null) {
      throw new UserLoginExistsException(login);
    }

    Cliente cliente = new Cliente();
    String encryptedPassword = passwordEncoder.encode(password);

    cliente.setNombre(nombre);
    cliente.setApellido(apellido);
    cliente.setTelefono(telefono);
    cliente.setFechaNacimiento(fechaNacimiento);
    cliente.setEmail(email);
    cliente.setLogin(login);
    cliente.setPassword(encryptedPassword);
    cliente.setCitas(citas);
    cliente.setPrimeraCita(primeraCita);
    cliente.setActivo(false); // Cliente inactivo hasta que confirme el correo

    usuarioDAO.create(cliente);
    return cliente; // Retornamos el cliente creado para usar su ID en la confirmación
  }

  // Confirmar registro del cliente mediante token
  @Transactional(readOnly = false)
  public void confirmarRegistro(String userId) throws NotFoundException {
    Usuario usuario = usuarioDAO.findById(Long.parseLong(userId));
    if (usuario == null || !(usuario instanceof Cliente)) {
      throw new NotFoundException(userId, Usuario.class);
    }

    Cliente cliente = (Cliente) usuario;
    cliente.setActivo(true); // Activamos al cliente
    usuarioDAO.update(cliente);
  }

  // Registrar Empleado con todos los campos adicionales
  @Transactional(readOnly = false)
  public void registerEmpleado(String nombre, String apellido, String telefono, LocalDate fechaNacimiento,
                               String puesto, String email, String login, String password, double salario,
                               String contrato, String horario, String descripcion)
          throws UserLoginExistsException {
    if (usuarioDAO.findByLogin(login) != null) {
      throw new UserLoginExistsException(login);
    }

    Empleado empleado = new Empleado();
    String encryptedPassword = passwordEncoder.encode(password);

    empleado.setNombre(nombre);
    empleado.setApellido(apellido);
    empleado.setTelefono(telefono);
    empleado.setFechaNacimiento(fechaNacimiento); // Establecer fecha de nacimiento
    empleado.setEmail(email); // Establecer email
    empleado.setLogin(login);
    empleado.setPassword(encryptedPassword);
    empleado.setPuesto(puesto);
    empleado.setSalario(salario);
    empleado.setContrato(contrato);
    empleado.setHorario(horario); // Nuevo campo horario
    empleado.setDescripcion(descripcion); // Nuevo campo descripción

    usuarioDAO.create(empleado);
  }

  // Registrar Jefe
  @Transactional(readOnly = false)
  public void registerJefe(String nombre, String apellido, String telefono, String email, String login,
                           String password) throws UserLoginExistsException {
    if (usuarioDAO.findByLogin(login) != null) {
      throw new UserLoginExistsException(login);
    }

    Jefe jefe = new Jefe();
    String encryptedPassword = passwordEncoder.encode(password);

    jefe.setNombre(nombre);
    jefe.setApellido(apellido);
    jefe.setTelefono(telefono);
    jefe.setEmail(email); // Establecer email
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

  @Transactional(readOnly = false)
  public AccountDTO updateProfile(AccountDTO accountDTO) throws NotFoundException, OperationNotAllowed {
    // Obtener el usuario autenticado
    String currentUserLogin = SecurityUtils.getCurrentUserLogin();
    if (currentUserLogin == null) {
      throw new OperationNotAllowed("No se puede identificar al usuario actual");
    }

    Usuario user = usuarioDAO.findByLogin(currentUserLogin);
    if (user == null) {
      throw new NotFoundException("Usuario no encontrado: " + currentUserLogin, Usuario.class);
    }

    // Validar que el ID proporcionado en el DTO coincide con el usuario autenticado
    if (!user.getId().equals(accountDTO.getId())) {
      throw new OperationNotAllowed("No puedes editar el perfil de otro usuario");
    }

    // Validar que el email no esté en uso por otro usuario
    if (accountDTO.getEmail() != null && !accountDTO.getEmail().equals(user.getEmail())) {
      Usuario existingUser = usuarioDAO.findByEmail(accountDTO.getEmail());
      if (existingUser != null) {
        throw new OperationNotAllowed("El email ya está en uso por otro usuario");
      }
    }

    // Validar que el login no pueda ser cambiado
    if (accountDTO.getLogin() != null && !accountDTO.getLogin().equals(user.getLogin())) {
      throw new OperationNotAllowed("No se permite cambiar el login del usuario");
    }

    // Actualizar los datos permitidos
    if (accountDTO.getNombre() != null) user.setNombre(accountDTO.getNombre());
    if (accountDTO.getApellido() != null) user.setApellido(accountDTO.getApellido());
    if (accountDTO.getEmail() != null) user.setEmail(accountDTO.getEmail());
    if (accountDTO.getTelefono() != null) user.setTelefono(accountDTO.getTelefono());
    if (accountDTO.getFechaNacimiento() != null) user.setFechaNacimiento(accountDTO.getFechaNacimiento());

    // Campos adicionales para empleados
    if (user instanceof Empleado empleado) {
      if (accountDTO.getHorario() != null) empleado.setHorario(accountDTO.getHorario());
      if (accountDTO.getDescripcion() != null) empleado.setDescripcion(accountDTO.getDescripcion());
    }

    // Guardar los cambios
    usuarioDAO.update(user);

    // Retornar los datos actualizados
    return new AccountDTO(user);
  }


}
