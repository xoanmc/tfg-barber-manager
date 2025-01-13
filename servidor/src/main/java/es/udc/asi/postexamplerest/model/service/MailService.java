package es.udc.asi.postexamplerest.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${properties.clientHost}")
    private String clientHost;

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    /**
     * Envía un correo de confirmación al destinatario proporcionado.
     *
     * @param to      Dirección de correo electrónico del destinatario.
     * @param subject Asunto del correo.
     * @param text    Contenido del correo.
     */
    @Async
    public void sendConfirmationEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);

            // Log para confirmar envío exitoso
            logger.info("Correo de confirmación enviado a: {}", to);
        } catch (Exception e) {
            // Log para capturar cualquier error al enviar el correo
            logger.error("Error al enviar el correo de confirmación a: {}", to, e);
        }
    }

    /**
     * Envía un correo de recuperación de contraseña al destinatario proporcionado.
     *
     * @param to      Dirección de correo electrónico del destinatario.
     * @param subject Asunto del correo.
     * @param text    Contenido del correo.
     */
    @Async
    public void sendPasswordRecoveryEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);

            // Log para confirmar envío exitoso
            logger.info("Correo de recuperación de contraseña enviado a: {}", to);
        } catch (Exception e) {
            // Log para capturar cualquier error al enviar el correo
            logger.error("Error al enviar el correo de recuperación de contraseña a: {}", to, e);
        }
    }

    /**
     * Envío de correo al cliente al realizar una reserva.
     */
    @Async
    public void sendReservationPendingEmail(String to, String nombreCliente, String detallesCita) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Reserva realizada: Pendiente de confirmación");
            message.setText(String.format(
                    "Estimad@ %s,\n\nHa realizado su reserva. Está pendiente de confirmación.\n\n%s\n\nGracias por confiar en nosotros.",
                    nombreCliente, detallesCita
            ));
            mailSender.send(message);
            logger.info("Correo de reserva pendiente enviado a: {}", to);
        } catch (Exception e) {
            logger.error("Error al enviar el correo de reserva pendiente a: {}", to, e);
        }
    }

    /**
     * Envío de correo al cliente al confirmar la reserva.
     */
    @Async
    public void sendReservationConfirmedEmail(String to, String nombreCliente, String detallesCita) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Reserva confirmada");
            message.setText(String.format(
                    "Estimad@ %s,\n\nSu reserva ha sido confirmada por el barbero.\n\n%s\n\n¡Nos vemos pronto!",
                    nombreCliente, detallesCita
            ));
            mailSender.send(message);
            logger.info("Correo de confirmación de cita enviado a: {}", to);
        } catch (Exception e) {
            logger.error("Error al enviar el correo de confirmación de cita a: {}", to, e);
        }
    }

    /**
     * Envío de correo al barbero al reservar la cita.
     */
    @Async
    public void sendBarberNotificationEmail(String to, String nombreBarbero, String nombreCliente, String detallesCita) {
        try {
            // Enlace para que el barbero confirme la cita
            String urlConfirmacion = clientHost + "/empleadoCitas";  // Utiliza el host del cliente

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Nueva cita pendiente de confirmación");
            message.setText(String.format(
                    "Hola %s,\n\nEl cliente %s ha realizado una reserva.\n\nDetalles de la cita:\n%s\n\n" +
                            "Por favor, haz clic en el siguiente enlace para acceder al panel de tus citas pendientes:\n%s\n\nGracias.",
                    nombreBarbero, nombreCliente, detallesCita, urlConfirmacion
            ));
            mailSender.send(message);
            logger.info("Correo de notificación enviado al barbero: {}", to);
        } catch (Exception e) {
            logger.error("Error al enviar el correo al barbero: {}", to, e);
        }
    }
}
