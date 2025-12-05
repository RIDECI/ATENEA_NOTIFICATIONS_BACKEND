package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.SendEmailNotificationUseCase;
import edu.dosw.rideci.application.port.out.EmailNotificationPort;
import edu.dosw.rideci.application.port.out.UserRepositoryPort;
import edu.dosw.rideci.domain.model.EmailNotification;
import edu.dosw.rideci.domain.model.Enum.EmailSendStatus;
import edu.dosw.rideci.domain.model.Enum.EmailType;
import edu.dosw.rideci.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Application service that orchestrates email notification sending.
 */
@Service
@RequiredArgsConstructor
public class SendEmailNotificationService implements SendEmailNotificationUseCase {

    private final EmailNotificationPort emailNotificationPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public EmailNotification sendEmail(
            UUID userId,
            EmailType emailType,
            Map<String, String> templateData
    ) {
        // 1. Get user from repository
        User user = userRepositoryPort.findById(userId.toString())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // 2. Build subject and body from template
        String subject = buildSubject(emailType, templateData);
        String body = buildBody(emailType, templateData);

        // 3. Create domain EmailNotification
        EmailNotification emailNotification = EmailNotification.builder()
                .user(user)
                .emailType(emailType)
                .subject(subject)
                .emailBody(body)
                .timestamp(LocalDateTime.now())
                .sendStatus(EmailSendStatus.PENDING)
                .build();

        // 4. Delegate real sending to the outbound port
        emailNotificationPort.sendEmail(emailNotification);

        return emailNotification;
    }

    private String buildSubject(EmailType type, Map<String, String> data) {
        // TODO: aquí puedes usar las plantillas “quemadas”
        return switch (type) {
            case VERIFICATION_CODE -> "Your verification code";
            case TRIP_CONFIRMATION -> "Trip confirmed";
            case PAYMENT_INVOICE -> "Your trip invoice";
            case SECURITY_REPORT -> "Security report";
            case TRIP_REMINDER -> "Trip reminder";
        };
    }

    private String buildBody(EmailType type, Map<String, String> data) {
        // TODO: aquí reemplazas {codigo}, {idViaje}, etc, usando el Map
        // Versión simple por ahora:
        return "Email type: " + type + " | data: " + data;
    }
}
