package edu.dosw.rideci.infrastructure.notification;

import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.service.EmailNotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationSenderImpl implements EmailNotificationSender {

    private final ZohoApiService zohoApiService;

    @Async
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
            String subject = buildSubject(notification);
            String body = buildBody(notification);

            zohoApiService.sendEmail(destinationEmail, subject, body);

            log.info("Correo enviado exitosamente vía API a {} para notificación id={}",
                    destinationEmail, notification.getId());
        } catch (Exception ex) {
            log.error("Error al enviar correo API a {} para notificación id={}: {}",
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
