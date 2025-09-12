package es.udc.asi.postexamplerest.config;

import es.udc.asi.postexamplerest.model.domain.*;
import es.udc.asi.postexamplerest.model.exception.UserLoginExistsException;
import es.udc.asi.postexamplerest.model.repository.*;
import es.udc.asi.postexamplerest.model.service.AboutPageInfoService;
import es.udc.asi.postexamplerest.model.service.HomePageInfoService;
import es.udc.asi.postexamplerest.model.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Configuration
@Profile("demo")
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
    private PeinadoDao peinadoDao;

    @Autowired
    private CitaDao citaDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void loadData() throws UserLoginExistsException {

        // registro de clientes
        Cliente pepe = userService.registerCliente(
                "pepe",
                "pereira",
                "+34 22222222",
                LocalDate.of(1998, 5, 15),
                "pepinho.pereira2025@gmail.com",
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

        // registro de empleados
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
        empleado1.setDescripcion("Especialista en cortes y afeitado tradicional.");
        empleado1.setDespedido(false);
        userDAO.create(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("lionel");
        empleado2.setApellido("messi");
        empleado2.setTelefono("+34 666666666");
        empleado2.setFechaNacimiento(LocalDate.of(1987, 6, 24));
        empleado2.setPuesto("barbero");
        empleado2.setEmail("lionel10.messi2025@gmail.com");
        empleado2.setLogin("messi");
        empleado2.setPassword(passwordEncoder.encode("messi"));
        empleado2.setSalario(3000.00);
        empleado2.setContrato("01/06/2022");
        empleado2.setDescripcion("Experto en cortes modernos y degradados.");
        empleado2.setDespedido(false);
        userDAO.create(empleado2);

        Empleado empleado3 = new Empleado();
        empleado3.setNombre("neymar");
        empleado3.setApellido("da silva");
        empleado3.setTelefono("+34 111111212");
        empleado3.setFechaNacimiento(LocalDate.of(1992, 2, 5));
        empleado3.setPuesto("barbero");
        empleado3.setEmail("ney.da.silva@gmail.com");
        empleado3.setLogin("neymar");
        empleado3.setPassword(passwordEncoder.encode("neymar"));
        empleado3.setSalario(3000.00);
        empleado3.setContrato("11/02/2023");
        empleado3.setDescripcion("Experto en cortes atrevidos.");
        empleado3.setDespedido(false);
        userDAO.create(empleado3);

        Empleado empleado4 = new Empleado();
        empleado4.setNombre("diego");
        empleado4.setApellido("maradona");
        empleado4.setTelefono("+34 777777777");
        empleado4.setFechaNacimiento(LocalDate.of(1960, 10, 30));
        empleado4.setPuesto("barbero");
        empleado4.setEmail("diego.maradona@gmail.com");
        empleado4.setLogin("maradona");
        empleado4.setPassword(passwordEncoder.encode("maradona"));
        empleado4.setSalario(2800.00);
        empleado4.setContrato("15/07/2023");
        empleado4.setDescripcion("Especialista en peinados vintage y clásicos con estilo.");
        empleado4.setDespedido(false);
        userDAO.create(empleado4);

        Empleado empleado5 = new Empleado();
        empleado5.setNombre("johan");
        empleado5.setApellido("cruyff");
        empleado5.setTelefono("+34 888888888");
        empleado5.setFechaNacimiento(LocalDate.of(1947, 4, 25));
        empleado5.setPuesto("barbero");
        empleado5.setEmail("johan.cruyff@gmail.com");
        empleado5.setLogin("cruyff");
        empleado5.setPassword(passwordEncoder.encode("cruyff"));
        empleado5.setSalario(2700.00);
        empleado5.setContrato("10/03/2023");
        empleado5.setDescripcion("Pionero en técnicas modernas de degradados y cortes minimalistas.");
        empleado5.setDespedido(false);
        userDAO.create(empleado5);

        Empleado empleado6 = new Empleado();
        empleado6.setNombre("zinedine");
        empleado6.setApellido("zidane");
        empleado6.setTelefono("+34 999999999");
        empleado6.setFechaNacimiento(LocalDate.of(1972, 6, 23));
        empleado6.setPuesto("barbero");
        empleado6.setEmail("zinedine.zidane@gmail.com");
        empleado6.setLogin("zidane");
        empleado6.setPassword(passwordEncoder.encode("zidane"));
        empleado6.setSalario(2900.00);
        empleado6.setContrato("01/09/2023");
        empleado6.setDescripcion("Conocido por sus cortes refinados y pulidos, perfectos para eventos especiales.");
        empleado6.setDespedido(false);
        userDAO.create(empleado6);

        //horarios
        horarioDao.create(new Horario(empleado1, "Lunes", LocalTime.of(9, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado1, "Martes", LocalTime.of(9, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado1, "Miércoles", LocalTime.of(9, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado1, "Jueves", LocalTime.of(9, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado1, "Viernes", LocalTime.of(9, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado1, "Sábado", LocalTime.of(9, 0), LocalTime.of(18, 0)));

        horarioDao.create(new Horario(empleado2, "Lunes", LocalTime.of(10, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado2, "Martes", LocalTime.of(10, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado2, "Miércoles", LocalTime.of(10, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado2, "Jueves", LocalTime.of(10, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado2, "Viernes", LocalTime.of(10, 0), LocalTime.of(19, 0)));

        horarioDao.create(new Horario(empleado3, "Lunes", LocalTime.of(9, 30), LocalTime.of(17, 30)));
        horarioDao.create(new Horario(empleado3, "Martes", LocalTime.of(9, 30), LocalTime.of(17, 30)));
        horarioDao.create(new Horario(empleado3, "Jueves", LocalTime.of(11, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado3, "Viernes", LocalTime.of(11, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado3, "Sábado", LocalTime.of(10, 0), LocalTime.of(15, 0)));

        horarioDao.create(new Horario(empleado4, "Lunes", LocalTime.of(9, 0), LocalTime.of(17, 0)));
        horarioDao.create(new Horario(empleado4, "Martes", LocalTime.of(9, 0), LocalTime.of(17, 0)));
        horarioDao.create(new Horario(empleado4, "Miércoles", LocalTime.of(9, 0), LocalTime.of(17, 0)));
        horarioDao.create(new Horario(empleado4, "Jueves", LocalTime.of(9, 0), LocalTime.of(17, 0)));
        horarioDao.create(new Horario(empleado4, "Viernes", LocalTime.of(9, 0), LocalTime.of(17, 0)));

        horarioDao.create(new Horario(empleado5, "Martes", LocalTime.of(10, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado5, "Miércoles", LocalTime.of(10, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado5, "Jueves", LocalTime.of(10, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado5, "Viernes", LocalTime.of(10, 0), LocalTime.of(18, 0)));
        horarioDao.create(new Horario(empleado5, "Sábado", LocalTime.of(9, 0), LocalTime.of(14, 0)));

        horarioDao.create(new Horario(empleado6, "Lunes", LocalTime.of(11, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado6, "Miércoles", LocalTime.of(11, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado6, "Viernes", LocalTime.of(11, 0), LocalTime.of(19, 0)));
        horarioDao.create(new Horario(empleado6, "Sábado", LocalTime.of(10, 0), LocalTime.of(17, 0)));

        // registro de jefes
        userService.registerJefe(
                "lucia",
                "lopez",
                "+34 555555555",
                "lucia.lopez@gmail.com", // Email
                "lucia",
                "lucia"
        );

        // cargar datos por defecto en HomePageInfo
        if (homePageInfoDao.find() == null) {
            HomePageInfo homePageInfo = new HomePageInfo("TFG Barber", "La mejor barbería de la ciudad.");
            homePageInfoService.updateHomePageInfo(homePageInfo);
            System.out.println("Datos por defecto de HomePageInfo cargados.");
        } else {
            System.out.println("HomePageInfo ya está cargado.");
        }

        // cargar servicios predeterminados
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

        // cargar datos por defecto de AboutPageInfo
        if (aboutPageInfoDao.find() == null) {
            AboutPageInfo aboutPageInfo = new AboutPageInfo();
            aboutPageInfo.setDescripcion("Nuestra barbería nació con la misión de ofrecer un espacio donde tradición y " +
                    "modernidad se encuentren para brindar un servicio excepcional. Desde nuestros inicios, nos hemos " +
                    "dedicado a perfeccionar el arte del corte de cabello y el afeitado clásico, con un equipo de " +
                    "profesionales apasionados por su oficio. Con cada visita, nuestros clientes disfrutan de una " +
                    "experiencia única.");
            aboutPageInfo.setImagen(null);  // No hay imagen por defecto
            aboutPageInfoService.updateAboutPageInfo(aboutPageInfo, null);
            System.out.println("Datos por defecto de AboutPageInfo cargados.");
        } else {
            System.out.println("AboutPageInfo ya está cargado.");
        }

        // cargar peinados predeterminados
        if (peinadoDao.findByTendenciaTrue().isEmpty()) {
            Peinado modernPompadour = new Peinado();
            modernPompadour.setNombre("The Modern Pompadour");
            modernPompadour.setDescripcionTendencias("Un clásico atemporal con un toque moderno, el pompadour sigue siendo una fuerza dominante en la moda capilar masculina.");
            modernPompadour.setDescripcionRecomendador("Este peinado añade altura y estructura, equilibrando bien los rostros con ángulos marcados.");
            modernPompadour.setEstructurasFaciales(List.of(EstructuraFacial.OVALADA, EstructuraFacial.CUADRADO, EstructuraFacial.DIAMANTE, EstructuraFacial.RECTANGULAR));
            modernPompadour.setUrlImagen("https://example.com/pompadour.jpg");
            modernPompadour.setTendencia(false);
            peinadoDao.save(modernPompadour);

            Peinado texturedCrop = new Peinado();
            texturedCrop.setNombre("Textured Crop");
            texturedCrop.setDescripcionTendencias("El corte de pelo texturizado sigue ganando popularidad, ya que ofrece una opción elegante que requiere poco mantenimiento.");
            texturedCrop.setDescripcionRecomendador("El volumen y la textura añaden dimensión, suavizando las líneas duras o redondeadas.");
            texturedCrop.setEstructurasFaciales(List.of(EstructuraFacial.REDONDA, EstructuraFacial.CUADRADO, EstructuraFacial.TRIANGULO));
            texturedCrop.setUrlImagen("https://example.com/textured_crop.jpg");
            texturedCrop.setTendencia(true);
            peinadoDao.save(texturedCrop);

            Peinado messyQuiff = new Peinado();
            messyQuiff.setNombre("Messy Quiff");
            messyQuiff.setDescripcionTendencias("Para quienes prefieren un look ligeramente desordenado, el messy quiff es la opción.");
            messyQuiff.setDescripcionRecomendador("El aspecto desordenado añade carácter al rostro y complementa los ángulos marcados o alargados.");
            messyQuiff.setEstructurasFaciales(List.of(EstructuraFacial.OVALADA, EstructuraFacial.CORAZON, EstructuraFacial.RECTANGULAR));
            messyQuiff.setUrlImagen("https://example.com/messy_quiff.jpg");
            messyQuiff.setTendencia(true);
            peinadoDao.save(messyQuiff);

            Peinado midLengthWaves = new Peinado();
            midLengthWaves.setNombre("Mid Length Waves");
            midLengthWaves.setDescripcionTendencias("Este peinado desprende un aire playero y relajado, perfecto para el hombre moderno.");
            midLengthWaves.setDescripcionRecomendador("Las ondas medias equilibran los rostros más angulares y proporcionan un aire relajado.");
            midLengthWaves.setEstructurasFaciales(List.of(EstructuraFacial.TRIANGULO_INVERTIDO, EstructuraFacial.CORAZON, EstructuraFacial.OVALADO_LARGO));
            midLengthWaves.setUrlImagen("https://example.com/mid_length_waves.jpg");
            midLengthWaves.setTendencia(true);
            peinadoDao.save(midLengthWaves);

            Peinado sleekSidePart = new Peinado();
            sleekSidePart.setNombre("Sleek Side Part");
            sleekSidePart.setDescripcionTendencias("Ideal para quienes prefieren un look sofisticado y clásico, la raya al lado es adecuada para distintos tipos de cabello y formas de cara.");
            sleekSidePart.setDescripcionRecomendador("La raya al lado estiliza los rostros rectos y añade simetría, proporcionando un aire sofisticado.");
            sleekSidePart.setEstructurasFaciales(List.of(EstructuraFacial.OVALADA, EstructuraFacial.CUADRADO, EstructuraFacial.RECTANGULAR, EstructuraFacial.TRIANGULO_INVERTIDO));
            sleekSidePart.setUrlImagen("https://example.com/sleek_side_part.jpg");
            sleekSidePart.setTendencia(true);
            peinadoDao.save(sleekSidePart);

            Peinado buzzCut = new Peinado();
            buzzCut.setNombre("Buzz Cut");
            buzzCut.setDescripcionTendencias("Ideal para quienes buscan un estilo sencillo y fresco.");
            buzzCut.setDescripcionRecomendador("Este corte minimalista destaca las características faciales y resalta los ángulos fuertes.");
            buzzCut.setEstructurasFaciales(List.of(EstructuraFacial.OVALADO_LARGO, EstructuraFacial.DIAMANTE, EstructuraFacial.CUADRADO, EstructuraFacial.CORAZON));
            buzzCut.setUrlImagen("https://example.com/buzz_cut.jpg");
            buzzCut.setTendencia(true);
            peinadoDao.save(buzzCut);

            Peinado taperedFade = new Peinado();
            taperedFade.setNombre("Tapered Fade");
            taperedFade.setDescripcionTendencias("El tapered fade con longitud en la parte superior ofrece un aspecto limpio y pulido a la vez que permite versatilidad en el peinado.");
            taperedFade.setDescripcionRecomendador("El fade progresivo da definición y estructura, ideal para rostros que necesitan equilibrio.");
            taperedFade.setEstructurasFaciales(List.of(EstructuraFacial.OVALADA, EstructuraFacial.CUADRADO, EstructuraFacial.TRIANGULO_INVERTIDO));
            taperedFade.setUrlImagen("https://example.com/tapered_fade.jpg");
            taperedFade.setTendencia(true);
            peinadoDao.save(taperedFade);

            Peinado modernMullet = new Peinado();
            modernMullet.setNombre("Modern Mullet");
            modernMullet.setDescripcionTendencias("Combina lados más cortos con un largo a capas y texturizado en la parte trasera, ofreciendo una estética elegante pero vanguardista.");
            modernMullet.setDescripcionRecomendador("Este peinado juega con el volumen en la parte trasera, aportando dimensión y un look vanguardista.");
            modernMullet.setEstructurasFaciales(List.of(EstructuraFacial.RECTANGULAR, EstructuraFacial.TRIANGULO, EstructuraFacial.DIAMANTE));
            modernMullet.setUrlImagen("https://example.com/modern_mullet.jpg");
            modernMullet.setTendencia(true);
            peinadoDao.save(modernMullet);

            Peinado naturalWaves = new Peinado();
            naturalWaves.setNombre("Natural Waves and Curls");
            naturalWaves.setDescripcionTendencias("Los hombres están haciendo hincapié en las ondas y los rizos, que trabajan con el movimiento natural del cabello.");
            naturalWaves.setDescripcionRecomendador("Los rizos naturales aportan suavidad y movimiento, equilibrando las líneas rectas y resaltando los rostros alargados.");
            naturalWaves.setEstructurasFaciales(List.of(EstructuraFacial.TRIANGULO_INVERTIDO, EstructuraFacial.REDONDA, EstructuraFacial.OVALADO_LARGO, EstructuraFacial.CUADRADO));
            naturalWaves.setUrlImagen("https://example.com/natural_waves.jpg");
            naturalWaves.setTendencia(true);
            peinadoDao.save(naturalWaves);

            Peinado texturedUndercuts = new Peinado();
            texturedUndercuts.setNombre("Textured Undercuts");
            texturedUndercuts.setDescripcionTendencias("Los undercuts siguen siendo los favoritos de los hombres, pero con un énfasis en la textura y dimensión.");
            texturedUndercuts.setDescripcionRecomendador("El contraste entre los lados rapados y la textura superior resalta los rasgos faciales sin endurecerlos demasiado.");
            texturedUndercuts.setEstructurasFaciales(List.of(EstructuraFacial.OVALADA, EstructuraFacial.CUADRADO, EstructuraFacial.RECTANGULAR, EstructuraFacial.CORAZON));
            texturedUndercuts.setUrlImagen("https://example.com/textured_undercuts.jpg");
            texturedUndercuts.setTendencia(false);
            peinadoDao.save(texturedUndercuts);

            System.out.println("Peinados en tendencia cargados correctamente.");
        } else {
            System.out.println("Los peinados en tendencia ya están cargados.");
        }

        List<Servicio> servicios = serviciosDao.findAll();
        Servicio cortar = servicios.stream().filter(s -> s.getNombre().equals("Cortar")).findFirst().orElseThrow(() -> new RuntimeException("Servicio 'Cortar' no encontrado"));
        Servicio afeitar = servicios.stream().filter(s -> s.getNombre().equals("Afeitar")).findFirst().orElseThrow(() -> new RuntimeException("Servicio 'Afeitar' no encontrado"));
        Servicio cortarAfeitar = servicios.stream().filter(s -> s.getNombre().equals("Cortar + Afeitar")).findFirst().orElseThrow(() -> new RuntimeException("Servicio 'Cortar + Afeitar' no encontrado"));

        // citas precargadas
        Cita cita1 = new Cita(null, pepe, empleado2, LocalDate.of(2025, 8, 25), LocalTime.of(10, 0), "Confirmada", cortar);
        cita1.setPreferencias("Sin preferencias");
        citaDao.create(cita1);

        Cita cita2 = new Cita(null, pepe, empleado2, LocalDate.of(2025, 8, 26), LocalTime.of(11, 30), "Confirmada", afeitar);
        cita2.setPreferencias("Prefiere barba perfilada");
        citaDao.create(cita2);

        Cita cita3 = new Cita(null, pepe, empleado2, LocalDate.of(2025, 8, 27), LocalTime.of(15, 0), "Confirmada", cortarAfeitar);
        cita3.setPreferencias("Solicita corte clásico");
        citaDao.create(cita3);

        Cita cita4 = new Cita(null, pepe, empleado2, LocalDate.of(2025, 8, 28), LocalTime.of(16, 0), "Confirmada", cortar);
        cita4.setPreferencias("Corte moderno degradado");
        citaDao.create(cita4);

        Cita cita5 = new Cita(null, pepe, empleado2, LocalDate.of(2025, 8, 29), LocalTime.of(9, 0), "Confirmada", afeitar);
        cita5.setPreferencias("Afeitado completo con toalla caliente");
        citaDao.create(cita5);

        Cita cita6 = new Cita(null, maria, empleado2, LocalDate.of(2025, 8, 30), LocalTime.of(10, 0), "Confirmada", cortar);
        cita6.setPreferencias("Sin preferencias");
        citaDao.create(cita6);

        Cita cita7 = new Cita(null, maria, empleado2, LocalDate.of(2025, 8, 31), LocalTime.of(11, 30), "Confirmada", afeitar);
        cita7.setPreferencias("Sin especificaciones");
        citaDao.create(cita7);

        Cita cita8 = new Cita(null, maria, empleado2, LocalDate.of(2025, 9, 1), LocalTime.of(14, 0), "Confirmada", cortarAfeitar);
        cita8.setPreferencias("Solicita un afeitado relajante");
        citaDao.create(cita8);

        Cita cita9 = new Cita(null, maria, empleado2, LocalDate.of(2025, 9, 2), LocalTime.of(15, 0), "Confirmada", cortar);
        cita9.setPreferencias("Prefiere un corte limpio y fresco");
        citaDao.create(cita9);

        Cita cita10 = new Cita(null, maria, empleado2, LocalDate.of(2025, 9, 3), LocalTime.of(9, 30), "Confirmada", afeitar);
        cita10.setPreferencias("Afeitado suave y natural");
        citaDao.create(cita10);

        Cita cita11 = new Cita(null, maria, empleado2, LocalDate.of(2025, 2, 28), LocalTime.of(16, 30), "Confirmada", afeitar);
        cita10.setPreferencias("sin preferencias");
        citaDao.create(cita11);

        Cita cita12 = new Cita(null, maria, empleado2, LocalDate.of(2025, 2, 28), LocalTime.of(17, 0), "Confirmada", afeitar);
        cita10.setPreferencias("sin preferencias");
        citaDao.create(cita12);

        Cita cita13 = new Cita(null, maria, empleado2, LocalDate.of(2025, 2, 28), LocalTime.of(18, 0), "Confirmada", afeitar);
        cita10.setPreferencias("sin preferencias");
        citaDao.create(cita13);

        Cita cita14 = new Cita(null, maria, empleado2, LocalDate.of(2025, 2, 28), LocalTime.of(10, 30), "Confirmada", afeitar);
        cita10.setPreferencias("sin preferencias");
        citaDao.create(cita14);

        Cita cita15 = new Cita(null, maria, empleado2, LocalDate.of(2025, 2, 28), LocalTime.of(11, 30), "Confirmada", afeitar);
        cita10.setPreferencias("sin preferencias");
        citaDao.create(cita15);

        Cita cita16 = new Cita(null, maria, empleado2, LocalDate.of(2025, 2, 28), LocalTime.of(12, 0), "Confirmada", afeitar);
        cita10.setPreferencias("sin preferencias");
        citaDao.create(cita16);

        Cita cita17 = new Cita(null, maria, empleado2, LocalDate.of(2025, 2, 28), LocalTime.of(13, 0), "Confirmada", afeitar);
        cita10.setPreferencias("sin preferencias");
        citaDao.create(cita17);

        Cita cita18 = new Cita(null, maria, empleado2, LocalDate.of(2025, 2, 28), LocalTime.of(13, 30), "Confirmada", afeitar);
        cita10.setPreferencias("sin preferencias");
        citaDao.create(cita18);

        Cita cita19 = new Cita(null, maria, empleado2, LocalDate.of(2025, 2, 28), LocalTime.of(15, 0), "Confirmada", afeitar);
        cita10.setPreferencias("sin preferencias");
        citaDao.create(cita19);

        Cita cita20 = new Cita(null, maria, empleado2, LocalDate.of(2025, 2, 28), LocalTime.of(15, 30), "Confirmada", afeitar);
        cita10.setPreferencias("sin preferencias");
        citaDao.create(cita20);

        System.out.println("10 citas confirmadas con Lionel Messi creadas correctamente.");
    }
}
