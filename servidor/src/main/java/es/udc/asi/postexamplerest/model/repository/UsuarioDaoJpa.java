package es.udc.asi.postexamplerest.model.repository;

import es.udc.asi.postexamplerest.model.domain.Cliente;
import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.domain.Empleado;
import es.udc.asi.postexamplerest.model.repository.util.GenericDaoJpa;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UsuarioDaoJpa extends GenericDaoJpa implements UsuarioDao {

  @Override
  public void create(Usuario usuario) {
    entityManager.persist(usuario);
  }

  @Override
  public void update(Usuario usuario) {
    entityManager.merge(usuario);
  }

  @Override
  public void delete(Usuario usuario) {
    entityManager.remove(usuario);
  }

  @Override
  public List<Empleado> findAllEmpleados() {
    return entityManager.createQuery("from Empleado", Empleado.class).getResultList();
  }

  @Override
  public List<Empleado> findAllBarberos() {
    return entityManager.createQuery("from Empleado e where e.puesto = :puesto", Empleado.class)
      .setParameter("puesto", "barbero")
      .getResultList();
  }

  @Override
  public List<Cliente> findAllClientes() {
    return entityManager.createQuery("from Cliente", Cliente.class).getResultList();
  }

  @Override
  public Usuario findById(Long id) {
    return entityManager.find(Usuario.class, id);
  }

  @Override
  public Cliente findClienteById(Long id) {
    return entityManager.find(Cliente.class, id);
  }

  @Override
  public Usuario findByLogin(String login) {
    TypedQuery<Usuario> query = entityManager.createQuery("from Usuario u where u.login = :login", Usuario.class)
      .setParameter("login", login);
    return DataAccessUtils.singleResult(query.getResultList());
  }
}
