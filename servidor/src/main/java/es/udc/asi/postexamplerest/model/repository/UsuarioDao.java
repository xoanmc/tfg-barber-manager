package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Empleado;
import es.udc.asi.postexamplerest.model.domain.Cliente;
import es.udc.asi.postexamplerest.model.domain.Usuario;

import java.util.List;

public interface UsuarioDao {
    void create(Usuario usuario);

    void update(Usuario usuario);

    void delete(Usuario usuario);

    List<Empleado> findAllEmpleados();

    List<Empleado> findAllBarberos(); // Nuevo método

    List<Cliente> findAllClientes();

    Usuario findById(Long id);

    Usuario findByLogin(String login);

    Usuario findByEmail(String Email);

    Cliente findClienteById(Long id);

    Cliente findByConfirmationToken(String confirmationToken);

}
