package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.exception.NotFoundException;
import es.udc.asi.postexamplerest.model.exception.OperationNotAllowed;
import es.udc.asi.postexamplerest.model.exception.UserLoginExistsException;
import es.udc.asi.postexamplerest.model.service.UsuarioService;
import es.udc.asi.postexamplerest.model.service.dto.AccountDTO;
import es.udc.asi.postexamplerest.model.service.dto.EmpleadoListaDTO;
import es.udc.asi.postexamplerest.model.service.dto.ClienteListaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserResource {

  @Autowired
  private UsuarioService userService;

  @PreAuthorize("hasAuthority('JEFE')")
  @GetMapping("/clientes")
  public List<ClienteListaDTO> findAllClientes() {
    return userService.findAllClientes();
  }

  @PreAuthorize("hasAuthority('JEFE')")
  @GetMapping("/empleados")
  public List<EmpleadoListaDTO> findAllEmpleados() {
    return userService.findAllEmpleados();
  }

  @PreAuthorize("hasAuthority('CLIENTE')")
  @GetMapping("/barberos")
  public List<EmpleadoListaDTO> findAllBarberos() {
    return userService.findAllBarberos();
  }

  // Endpoint para subir la imagen de perfil
  @PostMapping("/upload/{userId}")
  public ResponseEntity<Map<String, String>> uploadProfileImage(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
      return ResponseEntity.badRequest().body(null); // Si el archivo está vacío, retornar un error
    }

    try {
      String imageUrl = userService.uploadProfileImage(userId, file);
      Map<String, String> response = new HashMap<>();
      response.put("imageUrl", imageUrl);
      return ResponseEntity.ok(response);
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(null);
    } catch (IOException e) {
      return ResponseEntity.status(500).body(null);
    }
  }

  // Endpoint para obtener la URL de la imagen de perfil
  @GetMapping("/profile-image/{userId}")
  public ResponseEntity<Map<String, String>> getProfileImage(@PathVariable Long userId) {
    try {
      String imageUrl = userService.getProfileImageUrl(userId);
      Map<String, String> response = new HashMap<>();
      response.put("imageUrl", imageUrl);
      return ResponseEntity.ok(response);
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(null);
    }
  }

  // Endpoint para obtener la información de un usuario por ID
  @GetMapping("/{id}")
  public AccountDTO findOne(@PathVariable Long id) throws NotFoundException {
    return userService.findById(id);
  }

  // Endpoint para eliminar un usuario por ID
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    userService.deleteById(id);
  }
}
