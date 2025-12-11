package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.SendEmailNotificationUseCase;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.service.EmailNotificationSender;
import edu.dosw.rideci.domain.service.UserEmailResolver;
import edu.dosw.rideci.infrastructure.notification.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailNotificationApplicationService implements SendEmailNotificationUseCase {

    private final EmailTemplateService templateService;
    private final EmailNotificationSender emailSender;
    private final UserEmailResolver userEmailResolver;

    @Override
    public void send(SendEmailNotificationCommand command) {
        String to = resolveDestinationEmail(command);
        NotificationType type = command.type();

        String subject;
        String body;

        switch (type) {

            // 1) Recuperación de contraseña/cuenta
            case PASSWORD_RECOVERY -> {
                subject = "Recuperación de contraseña";
                body = templateService.buildPasswordRecoveryEmail(command);
            }

            // 2) Registro exitoso y verificación de cuenta
            case USER_REGISTERED -> {
                subject = "Registro exitoso y verificación de cuenta";
                body = templateService.buildRegistrationVerificationEmail(command);
            }

            // 3) Aprobación / rechazo de verificación de conductor
            case DRIVER_VALIDATED -> {
                subject = "Resultado de verificación de conductor";
                body = templateService.buildDriverVerificationResultEmail(command);
            }

            // 4) Confirmación de reserva de viaje
            case TRIP_CREATED -> {
                subject = "Confirmación de reserva de viaje";
                body = templateService.buildTripBookingConfirmationEmail(command);
            }

            // 5) Cancelación o modificación de viaje
            case TRIP_UPDATED, TRIP_CANCELLED -> {
                subject = "Actualización de tu viaje";
                body = templateService.buildTripUpdateEmail(command);
            }

            // 6) Recordatorio de viaje próximo
            case UPCOMING_TRIP_REMINDER -> {
                subject = "Recordatorio de viaje próximo";
                body = templateService.buildTripReminderEmail(command);
            }

            // 7) Confirmación de pago recibido/completado
            case PAYMENT_CONFIRMED-> {
                subject = "Confirmación de pago";
                body = templateService.buildPaymentConfirmationEmail(command);
            }

            // 8) Alerta de activación del botón de emergencia
            case EMERGENCY_ALERT -> {
                subject = "Alerta de emergencia";
                body = templateService.buildEmergencyAlertEmail(command);
            }

            // 9) Aviso de suspensión o bloqueo de cuenta
            case ACCOUNT_SUSPENDED -> {
                subject = "Aviso de suspensión o bloqueo de cuenta";
                body = templateService.buildAccountSuspensionEmail(command);
            }

            // IMPORTANTE: ajusta / elimina los case que no existan en tu enum real.
            default -> throw new IllegalArgumentException(
                    "Tipo de notificación no soportado para email: " + type
            );
        }

        InAppNotification notification = InAppNotification.builder()
                .notificationId(UUID.randomUUID())
                .userId(command.userId())
                .title(subject)
                .message(body)
                .eventType(type)
                .priority(resolvePriority(type))
                .status(NotificationStatus.PENDING)
                .createdAt(OffsetDateTime.now())
                .build();

        emailSender.sendNotification(notification, to);
    }

    private String resolveDestinationEmail(SendEmailNotificationCommand cmd) {
        if (cmd.emailOverride() != null && !cmd.emailOverride().isBlank()) {
            return cmd.emailOverride();
        }
        if (cmd.userId() != null) {
            return userEmailResolver.resolveEmail(cmd.userId().toString());
        }
        throw new IllegalArgumentException("No se pudo resolver el correo destino");
    }

    private String resolvePriority(NotificationType type) {
        // Puedes ajustar la prioridad según el caso de uso
        return switch (type) {
            case EMERGENCY_ALERT -> "HIGH";
            case PAYMENT_CONFIRMED, PAYMENT_FAILED,
                 TRIP_CREATED, TRIP_UPDATED, TRIP_CANCELLED,
                 UPCOMING_TRIP_REMINDER -> "NORMAL";
            case PASSWORD_RECOVERY,
                 USER_REGISTERED,
                 DRIVER_VALIDATED,
                 ACCOUNT_SUSPENDED -> "NORMAL";
            default -> "LOW";
        };
    }
}
