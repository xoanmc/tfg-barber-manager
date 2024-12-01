package es.udc.asi.postexamplerest.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    /**
     * Envía un correo de confirmación al destinatario proporcionado.
     *
     * @param to      Dirección de correo electrónico del destinatario.
     * @param subject Asunto del correo.
     * @param text    Contenido del correo.
     */
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
}
