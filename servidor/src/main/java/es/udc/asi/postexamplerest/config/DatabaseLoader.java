package es.udc.asi.postexamplerest.config;

import es.udc.asi.postexamplerest.model.domain.*;
import es.udc.asi.postexamplerest.model.exception.UserLoginExistsException;
import es.udc.asi.postexamplerest.model.repository.*;
import es.udc.asi.postexamplerest.model.service.AboutPageInfoService;
import es.udc.asi.postexamplerest.model.service.HomePageInfoService;
import es.udc.asi.postexamplerest.model.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @Autowired
    private HorarioDao horarioDao;

    @Autowired
    private AboutPageInfoDao aboutPageInfoDao;

    @Autowired
    private AboutPageInfoService aboutPageInfoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void loadData() throws UserLoginExistsException {

        // Registro de clientes
        Cliente pepe = userService.registerCliente(
                "pepe",
                "pereira",
                "+34 22222222",
                LocalDate.of(1998, 5, 15),
                "pepe.perez@gmail.com",
                "pepe",
                "pepe",
                5,
                "01/01/2020",
                false // No enviar correo
        );
        pepe.setActivo(true);
        userDAO.update(pepe);

        Cliente maria = userService.registerCliente(
                "maria",
                "martinez",
                "+34 4444444",
                LocalDate.of(1993, 11, 25), // Fecha de nacimiento
                "maria.martinez.colombo.2025@gmail.com", // Email
                "maria",
                "maria",
                3,
                "15/03/2021",
                false // No enviar correo
        );
        maria.setActivo(true); // Marcar como activo
        userDAO.update(maria);

        // Registro de empleados
        Empleado empleado1 = new Empleado();
        empleado1.setNombre("ronaldo");
        empleado1.setApellido("nazario");
        empleado1.setTelefono("+34 333333333");
        empleado1.setFechaNacimiento(LocalDate.of(1976, 9, 22));
        empleado1.setPuesto("barbero");
        empleado1.setEmail("ronaldo.nazario@gmail.com");
        empleado1.setLogin("ronaldo");
        empleado1.setPassword(passwordEncoder.encode("ronaldo"));
        empleado1.setSalario(2500.00);
        empleado1.setContrato("01/01/2023");
        empleado1.setDescripcion("Especialista en cortes y afeitado tradicional");
        empleado1.setDespedido(false); // Inicializar explícitamente
        userDAO.create(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("lionel");
        empleado2.setApellido("messi");
        empleado2.setTelefono("+34 666666666");
        empleado2.setFechaNacimiento(LocalDate.of(1987, 6, 24));
        empleado2.setPuesto("barbero");
        empleado2.setEmail("lionel.messi@gmail.com");
        empleado2.setLogin("messi");
        empleado2.setPassword(passwordEncoder.encode("messi"));
        empleado2.setSalario(3000.00);
        empleado2.setContrato("01/06/2022");
        empleado2.setDescripcion("Experto en cortes modernos y degradados");
        empleado2.setDespedido(false); // Inicializar explícitamente
        userDAO.create(empleado2);

        // Asignar horarios a Ronaldo
        horarioDao.create(new Horario(empleado1, "Lunes", LocalTime.of(9, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado1, "Martes", LocalTime.of(9, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado1, "Miércoles", LocalTime.of(9, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado1, "Jueves", LocalTime.of(9, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado1, "Viernes", LocalTime.of(9, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado1, "Sábado", LocalTime.of(9, 0), LocalTime.of(18, 0)));

        // Asignar horarios a Lionel
        horarioDao.create(new Horario(empleado2, "Lunes", LocalTime.of(10, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado2, "Martes", LocalTime.of(10, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado2, "Miércoles", LocalTime.of(10, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado2, "Jueves", LocalTime.of(10, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado2, "Viernes", LocalTime.of(10, 0), LocalTime.of(19, 0)));


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

        // Cargar datos por defecto de AboutPageInfo si no existe
        if (aboutPageInfoDao.find() == null) {
            AboutPageInfo aboutPageInfo = new AboutPageInfo();
            aboutPageInfo.setDescripcion("Aquí va la historia inicial por defecto de la barbería.");
            aboutPageInfo.setImagen(null);  // No hay imagen por defecto
            aboutPageInfoService.updateAboutPageInfo(aboutPageInfo, null);
            System.out.println("Datos por defecto de AboutPageInfo cargados.");
        } else {
            System.out.println("AboutPageInfo ya está cargado.");
        }
    }
}
