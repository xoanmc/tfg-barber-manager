  package es.udc.asi.postexamplerest.security;

  import java.util.Collections;

  import es.udc.asi.postexamplerest.model.domain.Cliente;
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.security.core.GrantedAuthority;
  import org.springframework.security.core.authority.SimpleGrantedAuthority;
  import org.springframework.security.core.userdetails.UserDetails;
  import org.springframework.security.core.userdetails.UserDetailsService;
  import org.springframework.security.core.userdetails.UsernameNotFoundException;
  import org.springframework.stereotype.Component;
  import org.springframework.transaction.annotation.Transactional;

  import es.udc.asi.postexamplerest.model.domain.Usuario;
  import es.udc.asi.postexamplerest.model.repository.UsuarioDao;

  @Component
  public class MyUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    private UsuarioDao userDAO;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
      Usuario user = userDAO.findByLogin(login);
      if (user == null) {
        throw new UsernameNotFoundException("User " + login + " not found");
      }

      // Verificar si el usuario es un cliente y si está activo
      if (user instanceof Cliente cliente && !cliente.isActivo()) {
        throw new UsernameNotFoundException("User " + login + " has not confirmed registration yet.");
      }

      logger.info("Loaded user {} with authority {}", login, user.getAutoridad().name());
      return new CustomUserPrincipal(user);  // Asegúrate de devolver CustomUserPrincipal
    }
  }
