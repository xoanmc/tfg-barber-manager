package es.udc.asi.postexamplerest.security;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.crypto.SecretKey;

import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.repository.UsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import es.udc.asi.postexamplerest.config.Properties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider {
  private static final String AUTHORITIES_KEY = "auth";

  @Autowired
  private Properties properties;

  @Autowired
  private UsuarioDao userDAO;


  public boolean validateToken(String authToken) {
    SecretKey key = Keys.hmacShaKeyFor(properties.getJwtSecretKey().getBytes(StandardCharsets.UTF_8));
    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
    return true;
  }

  public Authentication getAuthentication(String authToken) {
    SecretKey key = Keys.hmacShaKeyFor(properties.getJwtSecretKey().getBytes(StandardCharsets.UTF_8));
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken).getBody();

    // Obtiene login (nombre de usuario) desde el token
    String login = claims.getSubject();
    GrantedAuthority authority = new SimpleGrantedAuthority(claims.get(AUTHORITIES_KEY).toString());
    Collection<GrantedAuthority> authorities = Collections.singleton(authority);

    // Carga usuario desde la base de datos
    Usuario usuario = userDAO.findByLogin(login); // Asegúrate de tener acceso a `userDAO` en esta clase
    if (usuario == null) {
      throw new UsernameNotFoundException("User " + login + " not found");
    }

    // Envuelve `Usuario` en `CustomUserPrincipal`
    CustomUserPrincipal customUserPrincipal = new CustomUserPrincipal(usuario);
    return new UsernamePasswordAuthenticationToken(customUserPrincipal, authToken, authorities);
  }


  public String createToken(Authentication authentication) {
    String authority = authentication.getAuthorities().iterator().next().toString();

    long now = (new Date()).getTime();
    Date validity = new Date(now + (properties.getJwtValidity() * 1000));

    SecretKey key = Keys.hmacShaKeyFor(properties.getJwtSecretKey().getBytes(StandardCharsets.UTF_8));
    return Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authority).signWith(key)
        .setExpiration(validity).compact();
  }

  public String createEmailVerificationToken(String userId) {
    long now = System.currentTimeMillis();
    Date expiryDate = new Date(now + 24 * 60 * 60 * 1000); // 24 horas

    SecretKey key = Keys.hmacShaKeyFor(properties.getJwtSecretKey().getBytes(StandardCharsets.UTF_8));
    return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(new Date(now))
            .setExpiration(expiryDate)
            .signWith(key)
            .compact();
  }

  public String validateEmailVerificationToken(String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(properties.getJwtSecretKey().getBytes(StandardCharsets.UTF_8));
      Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
      return claims.getSubject(); // Devuelve el ID del usuario
    } catch (Exception e) {
      throw new IllegalArgumentException("Token inválido o expirado");
    }
  }

}
