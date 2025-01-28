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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

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

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public void registerClient(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("email") String email,
            @RequestParam("telefono") String telefono,
            @RequestParam("fechaNacimiento") String fechaNacimiento,
            @RequestParam("login") String login,
            @RequestParam("password") String password,
            @RequestParam(value = "fotoPerfil", required = false) MultipartFile fotoPerfil)
            throws UserLoginExistsException, NotFoundException {

        // parsear fecha de nacimiento
        LocalDate parsedFechaNacimiento = LocalDate.parse(fechaNacimiento);

        Cliente cliente = userService.registerCliente(
                nombre,
                apellido,
                telefono,
                parsedFechaNacimiento,
                email,
                login,
                password,
                0, // Número inicial de citas
                null,
                true
        );

        // si se proporciona una foto de perfil, subirla y asociarla al cliente
        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            try {
                userService.uploadProfileImage(cliente.getId(), fotoPerfil);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la foto de perfil", e);
            }
        }
    }

    @GetMapping("/confirm-registration")
    public ResponseEntity<Void> confirmRegistration(@RequestParam("token") String token) {
        try {
            String login = tokenProvider.validateEmailVerificationToken(token);
            userService.confirmarRegistro(login);

            // redirige al frontend
            return ResponseEntity.status(302)
                    .header("Location", "http://localhost:1234/confirmRegistration?success=true")
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(302)
                    .header("Location", "http://localhost:1234/confirmRegistration?success=false&error=invalid_token")
                    .build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(302)
                    .header("Location", "http://localhost:1234/confirmRegistration?success=false&error=user_not_found")
                    .build();
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

    //manejo de recuperación de contraseña
    @PostMapping("/password-recovery")
    public ResponseEntity<Void> solicitarRecuperacion(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        logger.info("Solicitud recibida para recuperación de contraseña del email: {}", email);
        try {
            userService.enviarInstruccionesRecuperacion(email);
            logger.info("Correo de recuperación enviado con éxito.");
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            logger.warn("No se encontró usuario para el email: {}", email);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<Void> redirigirFormulario(@RequestParam("token") String token) {
        return ResponseEntity.status(302)
                .header("Location", "http://localhost:1234/resetPassword?token=" + token)
                .build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetearContraseña(@RequestBody Map<String, String> request) throws NotFoundException {
        String token = request.get("token");
        String newPassword = request.get("password");
        System.out.println("Token recibido: " + token);

        userService.resetearContraseña(token, newPassword);
        return ResponseEntity.noContent().build();
    }
}