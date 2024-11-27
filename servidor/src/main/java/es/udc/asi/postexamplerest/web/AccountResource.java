package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.config.Properties;
import es.udc.asi.postexamplerest.model.domain.Cliente;
import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.exception.NotFoundException;
import es.udc.asi.postexamplerest.model.exception.UserLoginExistsException;
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

  @PostMapping("/authenticate")
  public JWTToken authenticate(@Valid @RequestBody LoginDTO loginDTO) throws CredentialsAreNotValidException {

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginDTO.getLogin(), loginDTO.getPassword());
    try {
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
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

    // Registro del cliente
    Cliente cliente = userService.registerCliente(
            account.getNombre(),
            account.getApellido(),
            account.getTelefono(),
            account.getFechaNacimiento(),
            account.getEmail(),
            account.getLogin(),
            account.getPassword(),
            account.getCitas(),
            account.getPrimeraCita()
    );

    // Generar token de verificación
    String token = tokenProvider.createEmailVerificationToken(cliente.getId().toString());

    // Crear enlace de verificación
    String verificationUrl = properties.getClientHost() + "/confirm-email?token=" + token;

    // Enviar correo de confirmación
    String subject = "Confirma tu registro en Barbería";
    String text = "Haz clic en el siguiente enlace para confirmar tu registro: " + verificationUrl;

    mailService.sendConfirmationEmail(cliente.getEmail(), subject, text);
  }

  @GetMapping("/confirm-email")
  public String confirmEmail(@RequestParam("token") String token) {
    try {
      String userId = tokenProvider.validateEmailVerificationToken(token);
      userService.confirmarRegistro(userId);
      return "Registro confirmado exitosamente.";
    } catch (IllegalArgumentException e) {
      return "El enlace de confirmación es inválido o ha expirado.";
    } catch (NotFoundException e) {
      return "Usuario no encontrado.";
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
}