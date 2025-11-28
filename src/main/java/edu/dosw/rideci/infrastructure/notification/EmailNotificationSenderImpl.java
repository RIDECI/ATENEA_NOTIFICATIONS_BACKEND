package edu.dosw.rideci.infrastructure.notification;

import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.service.EmailNotificationSender; // 👈 IMPORT CORRECTO
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationSenderImpl implements EmailNotificationSender { // 👈 NOMBRE CORRECTO

    private final JavaMailSender mailSender;

    @Override
    public void sendNotification(InAppNotification notification, String destinationEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinationEmail);
            message.setSubject(buildSubject(notification));
            message.setText(buildBody(notification));

            mailSender.send(message);
            log.info("Email notification sent to {}", destinationEmail);
        } catch (Exception e) {
            log.error("Error sending email notification to {}: {}", destinationEmail, e.getMessage(), e);
        }
    }

    private String buildSubject(InAppNotification notification) {
        return "[RIDECI] " + notification.getTitle();
    }

    private String buildBody(InAppNotification notification) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hola,\n\n");
        sb.append(notification.getMessage()).append("\n\n");
        sb.append("Fecha: ").append(notification.getCreatedAt()).append("\n");
        sb.append("Estado: ").append(notification.getStatus()).append("\n\n");
        sb.append("Este es un mensaje automático de RIDECI.");
        return sb.toString();
    }
}
