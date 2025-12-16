package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.CreateEmergencyBroadcastUseCase;
import edu.dosw.rideci.application.port.out.NotificationRepositoryPort;
import edu.dosw.rideci.domain.model.Enum.BroadcastType;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.service.EmailNotificationSender;
import edu.dosw.rideci.domain.service.NotificationDomainService;
import edu.dosw.rideci.domain.service.UserEmailResolver;
import edu.dosw.rideci.infrastructure.notification.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateEmergencyBroadcastService implements CreateEmergencyBroadcastUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;
    private final NotificationDomainService notificationDomainService;
    private final EmailTemplateService emailTemplateService;
    private final EmailNotificationSender emailNotificationSender;
    private final UserEmailResolver userEmailResolver;

    @Override
    public List<InAppNotification> createEmergencyBroadcast(CreateEmergencyBroadcastCommand command) {
        List<InAppNotification> created = new ArrayList<>();

        if (command == null) {
            log.warn("createEmergencyBroadcast: command es null");
            return created;
        }

        List<String> targets = command.targetUserIds();
        if (targets == null || targets.isEmpty()) {
            log.warn("createEmergencyBroadcast: targetUserIds vacío, no hay destinatarios");
            return created;
        }

        for (String userIdStr : targets) {
            try {
                String htmlBody = emailTemplateService.buildAdminBroadcastEmail(
                        null,
                        "RideECI",
                        buildBroadcastTitle(command.broadcastType()),
                        command.emergencyMessage(),
                        null,
                        command.broadcastType().name(),
                        command.priorityLevel(),
                        OffsetDateTime.now(ZoneOffset.UTC)
                );

                UUID userUuid = null;
                try {
                    userUuid = UUID.fromString(userIdStr);
                } catch (IllegalArgumentException ex) {
                    log.warn("Broadcast: userId '{}' no es un UUID válido. " +
                            "Se enviará solo el correo si es posible.", userIdStr);
                }

                InAppNotification notification = InAppNotification.builder()
                        .notificationId(UUID.randomUUID())
                        .userId(userUuid)
                        .title(buildBroadcastTitle(command.broadcastType()))
                        .message(htmlBody)
                        .eventType(NotificationType.EMERGENCY_ALERT)
                        .priority(command.priorityLevel() != null ? command.priorityLevel() : "HIGH")
                        .status(NotificationStatus.UNREAD)
                        .createdAt(OffsetDateTime.now(ZoneOffset.UTC))
                        .build();

                notificationDomainService.initializeNotification(notification);

                if (userUuid != null) {
                    notificationRepositoryPort.save(notification);
                } else {
                    log.warn("Broadcast: notificación no se persiste porque userUuid es null (userIdStr={})", userIdStr);
                }

                created.add(notification);

                String destinationEmail = userEmailResolver.resolveEmail(userIdStr);
                emailNotificationSender.sendNotification(notification, destinationEmail);

            } catch (Exception ex) {
                log.error("Error procesando broadcast para userId {}: {}", userIdStr, ex.getMessage(), ex);
            }
        }

        return created;
    }

    private String buildBroadcastTitle(BroadcastType type) {
        if (type == null) {
            return "Aviso administrativo";
        }
        return switch (type) {
            case EMERGENCY -> "Alerta de emergencia";
            case HIGH_PRIORITY_ALERT -> "Alerta prioritaria";
            case SYSTEM_ANNOUNCEMENT -> "Anuncio del sistema";
            case MAINTENANCE_NOTICE -> "Aviso de mantenimiento";
            case ADMIN_ANNOUNCEMENT -> "Comunicado administrativo";
            case TEST_BROADCAST -> "Mensaje de prueba";
        };
    }
}
