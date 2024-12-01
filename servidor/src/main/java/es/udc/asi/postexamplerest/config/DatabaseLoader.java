package es.udc.asi.postexamplerest.config;

import es.udc.asi.postexamplerest.model.domain.Cliente;
import es.udc.asi.postexamplerest.model.domain.HomePageInfo;
import es.udc.asi.postexamplerest.model.domain.Servicio;
import es.udc.asi.postexamplerest.model.exception.UserLoginExistsException;
import es.udc.asi.postexamplerest.model.repository.HomePageInfoDao;
import es.udc.asi.postexamplerest.model.repository.UsuarioDao;
import es.udc.asi.postexamplerest.model.repository.ServiciosDao;
import es.udc.asi.postexamplerest.model.service.HomePageInfoService;
import es.udc.asi.postexamplerest.model.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Configuration
public class DatabaseLoader {

  @Autowired
  private UsuarioService userService;

  @Autowired
  private UsuarioDao userDAO;

  @Autowired
  private HomePageInfoService homePageInfoService;

  @Autowired
  private HomePageInfoDao homePageInfoDao;

  @Autowired
  private ServiciosDao serviciosDao;

  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void loadData() throws UserLoginExistsException {

    // Registro de clientes
    userService.registerCliente(
            "pepe",
            "perez",
            "+34 22222222",
            LocalDate.of(1988, 5, 15), // Fecha de nacimiento
            "pepe11.perez91@gmail.com", // Email
            "pepe",
            "pepe",
            5,
            "01/01/2020",
            false // No enviar correo
    );

    Cliente pepe = userDAO.findClienteById(userDAO.findByLogin("pepe").getId());

    userService.registerCliente(
            "maria",
            "martinez",
            "+34 4444444",
            LocalDate.of(1983, 11, 25), // Fecha de nacimiento
            "maria.martinez@gmail.com", // Email
            "maria",
            "maria",
            3,
            "15/03/2021",
            false // No enviar correo
    );

    Cliente maria = userDAO.findClienteById(userDAO.findByLogin("maria").getId());

    // Registro de empleados (incluidos los barberos)
    userService.registerEmpleado(
            "ronaldo",
            "nazario",
            "+34 333333333",
            LocalDate.of(1976, 9, 22), // Fecha de nacimiento
            "barbero",
            "ronaldo.nazario@gmail.com", // Email
            "ronaldo",
            "ronaldo",
            2500.00,
            "01/01/2023",
            "LUN-SAB 9:00-18:00",
            "Especialista en cortes y afeitado tradicional"
    );

    userService.registerEmpleado(
            "lionel",
            "messi",
            "+34 666666666",
            LocalDate.of(1987, 6, 24), // Fecha de nacimiento
            "barbero",
            "lionel.messi@gmail.com", // Email
            "messi",
            "messi",
            3000.00,
            "01/06/2022",
            "LUN-VIE 10:00-19:00",
            "Experto en cortes modernos y degradados"
    );

    // Registro de jefes
    userService.registerJefe(
            "lucia",
            "lopez",
            "+34 555555555",
            "lucia.lopez@gmail.com", // Email
            "lucia",
            "lucia"
    );

    // Cargar datos por defecto en la tabla de HomePageInfo
    if (homePageInfoDao.find() == null) {
      HomePageInfo homePageInfo = new HomePageInfo("TFG Barber", "La mejor barbería de la ciudad.");
      homePageInfoService.updateHomePageInfo(homePageInfo);
      System.out.println("Datos por defecto de HomePageInfo cargados.");
    } else {
      System.out.println("HomePageInfo ya está cargado.");
    }

    // Cargar servicios predeterminados si no existen
    if (serviciosDao.findAll().isEmpty()) {
      Servicio cortar = new Servicio("Cortar", "Corte de cabello estándar", 15.00);
      Servicio afeitar = new Servicio("Afeitar", "Afeitado tradicional con toalla caliente", 10.00);
      Servicio cortarAfeitar = new Servicio("Cortar + Afeitar", "Paquete completo de corte y afeitado", 22.00);

      serviciosDao.create(cortar);
      serviciosDao.create(afeitar);
      serviciosDao.create(cortarAfeitar);

      System.out.println("Servicios por defecto cargados.");
    } else {
      System.out.println("Los servicios ya están cargados.");
    }
  }
}
