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
                // 1) Construir HTML del correo con la plantilla de administración
                String htmlBody = emailTemplateService.buildAdminBroadcastEmail(
                        null,                               // userName (puedes mejorarlo luego)
                        "RideECI",                          // appName
                        buildBroadcastTitle(command.broadcastType()),
                        command.emergencyMessage(),
                        null,                               // reason: lo puede rellenar admin si quieres
                        command.broadcastType().name(),
                        command.priorityLevel(),
                        OffsetDateTime.now(ZoneOffset.UTC)
                );

                // 2) Intentar parsear el userId a UUID SOLO si el string lo permite
                UUID userUuid = null;
                try {
                    userUuid = UUID.fromString(userIdStr);
                } catch (IllegalArgumentException ex) {
                    // No tumbes todo por esto: lo registras y sigues, al menos el correo se envía.
                    log.warn("Broadcast: userId '{}' no es un UUID válido. " +
                            "Se enviará solo el correo si es posible.", userIdStr);
                }

                // 3) Crear notificación in-app (si tienes userUuid podrás guardar en BD)
                InAppNotification notification = InAppNotification.builder()
                        .notificationId(UUID.randomUUID())
                        .userId(userUuid)
                        .title(buildBroadcastTitle(command.broadcastType()))
                        // En in-app lo ideal es mensaje de texto, pero puedes guardar resumen o HTML.
                        .message(htmlBody)
                        .eventType(NotificationType.EMERGENCY_ALERT)
                        .priority(command.priorityLevel() != null ? command.priorityLevel() : "HIGH")
                        .status(NotificationStatus.UNREAD)
                        .createdAt(OffsetDateTime.now(ZoneOffset.UTC))
                        .build();

                notificationDomainService.initializeNotification(notification);

                // Si la BD exige userId != null y aquí es null, comenta esta línea mientras pruebas:
                if (userUuid != null) {
                    notificationRepositoryPort.save(notification);
                } else {
                    log.warn("Broadcast: notificación no se persiste porque userUuid es null (userIdStr={})", userIdStr);
                }

                created.add(notification);

                // 4) Resolver correo destino y enviar SIEMPRE correo
                String destinationEmail = userEmailResolver.resolveEmail(userIdStr);
                emailNotificationSender.sendNotification(notification, destinationEmail);

            } catch (Exception ex) {
                // NO tumbes todo el broadcast por un solo usuario
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
