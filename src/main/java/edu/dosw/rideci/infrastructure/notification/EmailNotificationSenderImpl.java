package edu.dosw.rideci.infrastructure.notification;

import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.service.EmailNotificationSender;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationSenderImpl implements EmailNotificationSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendNotification(InAppNotification notification, String destinationEmail) {
        if (destinationEmail == null || destinationEmail.isBlank()) {
            log.warn("No se envía correo: destinationEmail es null o vacío. Notificación id={}",
                    notification != null ? notification.getId() : null);
            return;
        }

        if (notification == null) {
            log.warn("No se envía correo: InAppNotification es null para destino={}", destinationEmail);
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setTo(destinationEmail);
            helper.setSubject(buildSubject(notification));
            helper.setText(buildBody(notification), true);
            helper.setFrom("rideci-email@rideci.online");

            mailSender.send(mimeMessage);
            log.info("Correo enviado exitosamente a {} para notificación id={}",
                    destinationEmail, notification.getId());
        } catch (Exception ex) {
            log.error("Error al enviar correo a {} para notificación id={}: {}",
                    destinationEmail,
                    notification.getId(),
                    ex.getMessage(),
                    ex);
        }
    }

    private String buildSubject(InAppNotification notification) {
        String title = notification.getTitle();
        if (title == null || title.isBlank()) {
            return "Notificación";
        }
        return title;
    }

    /**
     * Aquí asumimos que `notification.getMessage()` ya contiene el HTML
     * construido por `EmailTemplateService` (buildPasswordRecoveryEmail, etc.).
     */
    private String buildBody(InAppNotification notification) {
        String message = notification.getMessage();
        if (message == null || message.isBlank()) {
            return """
                <html>
                  <body>
                    <p>Hola,</p>
                    <p>Tienes una nueva notificación en RideECI.</p>
                  </body>
                </html>
                """;
        }
        return message;
    }
}
