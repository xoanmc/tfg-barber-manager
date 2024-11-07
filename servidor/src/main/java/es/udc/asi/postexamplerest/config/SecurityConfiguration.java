package es.udc.asi.postexamplerest.config;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import es.udc.asi.postexamplerest.security.JWTConfigurer;
import es.udc.asi.postexamplerest.security.MyAccessDeniedHandler;
import es.udc.asi.postexamplerest.security.MyUnauthorizedEntryPoint;
import es.udc.asi.postexamplerest.security.MyUserDetailsService;
import es.udc.asi.postexamplerest.security.TokenProvider;

/*
Establece los permisos y protege los endpoints de la API para que solo
los usuarios autorizados puedan acceder a los recursos
y realizar acciones específicas.
*/

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

  @Autowired
  private Properties properties;

  @Autowired
  private MyUnauthorizedEntryPoint myUnauthorizedEntryPoint;

  @Autowired
  private MyAccessDeniedHandler myAccessDeniedHandler;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private MyUserDetailsService myUserDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // @formatter:off
    http.csrf().disable().exceptionHandling()
      .authenticationEntryPoint(myUnauthorizedEntryPoint)
      .accessDeniedHandler(myAccessDeniedHandler).and()
      .headers().frameOptions().disable().and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .authorizeRequests()
      .antMatchers("/api/authenticate").permitAll() // Autenticación
      .antMatchers(HttpMethod.POST, "/api/register").permitAll() // Registro
      .antMatchers("/api/home").permitAll() // Permitir acceso público a la página de inicio
      .antMatchers("/api/update").hasAuthority("JEFE") // Solo jefes pueden actualizar la info
      .antMatchers("/api/about/**").permitAll() // Acceso público a la sección "about"
      .antMatchers("/api/reserve").permitAll()
      .antMatchers("/api/servicios/**").permitAll() // Acceso público a servicios
      .antMatchers(HttpMethod.GET, "/api/users/**").permitAll() // Ver usuarios
      .antMatchers(HttpMethod.POST, "/api/users/upload/**").permitAll() // Subida de archivos permitida
      .antMatchers("/**").authenticated() // Todas las demás rutas requieren autenticación
      .and()
      .apply(securityConfigurerAdapter());
    // @formatter:on
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**"); // Ignorar las solicitudes OPTIONS (CORS)
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedOrigins(properties.getClientHost());
      }
    };
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
    throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Autowired
  public void configureAuth(AuthenticationManagerBuilder auth) {
    try {
      auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder);
    } catch (Exception e) {
      throw new BeanInitializationException("SecurityConfiguration.configureAuth failed", e);
    }
  }

  private JWTConfigurer securityConfigurerAdapter() {
    return new JWTConfigurer(tokenProvider);
  }
}
