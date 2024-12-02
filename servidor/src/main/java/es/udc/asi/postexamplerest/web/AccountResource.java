package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.config.Properties;
import es.udc.asi.postexamplerest.model.domain.Cliente;
import es.udc.asi.postexamplerest.model.domain.Empleado;
import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.exception.NotFoundException;
import es.udc.asi.postexamplerest.model.exception.UserLoginExistsException;
import es.udc.asi.postexamplerest.model.repository.UsuarioDao;
import es.udc.asi.postexamplerest.model.service.MailService;
import es.udc.asi.postexamplerest.model.service.UsuarioService;
import es.udc.asi.postexamplerest.model.service.dto.AccountDTO;
import es.udc.asi.postexamplerest.model.service.dto.LoginDTO;
import es.udc.asi.postexamplerest.security.JWTToken;
import es.udc.asi.postexamplerest.security.TokenProvider;
import es.udc.asi.postexamplerest.web.exceptions.CredentialsAreNotValidException;
import es.udc.asi.postexamplerest.web.exceptions.RequestBodyNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * Este controlador maneja la autenticación, registro y gestión de cuentas de usuarios.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {
    private final Logger logger = LoggerFactory.getLogger(AccountResource.class);

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private Properties properties;

    @Autowired
    private UsuarioDao usuarioDAO;

    @PostMapping("/authenticate")
    public JWTToken authenticate(@Valid @RequestBody LoginDTO loginDTO) throws CredentialsAreNotValidException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getLogin(), loginDTO.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            Usuario usuario = usuarioDAO.findByLogin(loginDTO.getLogin());
            if (usuario instanceof Empleado && ((Empleado) usuario).isDespedido()) {
                throw new CredentialsAreNotValidException("El empleado ha sido despedido y no puede iniciar sesión.");
            }
            if (usuario instanceof Cliente && !((Cliente) usuario).isActivo()) {
                throw new CredentialsAreNotValidException("El cliente está inactivo y no puede iniciar sesión.");
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication);
            return new JWTToken(jwt);
        } catch (AuthenticationException e) {
            logger.warn(e.getMessage(), e);
            throw new CredentialsAreNotValidException(e.getMessage());
        }
    }

    @GetMapping("/account")
    public AccountDTO getAccount() {
        return userService.getCurrentUserWithAuthority();
    }

    @PostMapping("/register")
    public void registerClient(@Valid @RequestBody AccountDTO account, Errors errors)
            throws UserLoginExistsException, RequestBodyNotValidException {
        if (errors.hasErrors()) {
            throw new RequestBodyNotValidException(errors);
        }

        // Registrar el cliente, el correo se enviará desde UsuarioService si es necesario
        userService.registerCliente(
                account.getNombre(),
                account.getApellido(),
                account.getTelefono(),
                account.getFechaNacimiento(),
                account.getEmail(),
                account.getLogin(),
                account.getPassword(),
                account.getCitas(),
                account.getPrimeraCita(),
                true // Activar el envío de correo desde UsuarioService
        );
    }

    @GetMapping("/confirm-registration")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
        try {
            // Validar el token JWT y obtener el login del usuario
            String login = tokenProvider.validateEmailVerificationToken(token);
            logger.info("Token válido. Usuario login: {}", login);

            // Confirmar el registro del usuario
            userService.confirmarRegistro(login);

            // Responder con un mensaje de éxito
            return ResponseEntity.ok("Registro confirmado exitosamente.<br>Puedes cerrar esta ventana.");
        } catch (IllegalArgumentException e) {
            logger.error("Error de token inválido: {}", e.getMessage());
            return ResponseEntity.badRequest().body("El enlace de confirmación es inválido o ha expirado.");
        } catch (NotFoundException e) {
            logger.error("Usuario no encontrado: {}", e.getMessage());
            return ResponseEntity.status(404).body("Usuario no encontrado.");
        }
    }

    @PostMapping("/contratar")
    public void registerEmpleado(@Valid @RequestBody AccountDTO account, Errors errors)
            throws UserLoginExistsException, RequestBodyNotValidException {
        if (errors.hasErrors()) {
            throw new RequestBodyNotValidException(errors);
        }

        userService.registerEmpleado(
                account.getNombre(),
                account.getApellido(),
                account.getTelefono(),
                account.getFechaNacimiento(),
                account.getPuesto(),
                account.getEmail(),
                account.getLogin(),
                account.getPassword(),
                account.getSalario(),
                account.getContrato(),
                account.getHorario(),
                account.getDescripcion()
        );
    }

    @PatchMapping("/users/{id}/despedir")
    public ResponseEntity<Void> despedirEmpleado(@PathVariable Long id) throws NotFoundException {
        userService.despedirEmpleado(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}/bloquear")
    public ResponseEntity<Void> bloquearCliente(@PathVariable Long id) throws NotFoundException {
        userService.bloquearCliente(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}/activar")
    public ResponseEntity<Void> activarCliente(@PathVariable Long id) throws NotFoundException {
        userService.cambiarEstadoCliente(id, true);
        return ResponseEntity.noContent().build();
    }

}