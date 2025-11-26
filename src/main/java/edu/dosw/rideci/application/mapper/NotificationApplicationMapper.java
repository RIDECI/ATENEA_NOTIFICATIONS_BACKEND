package edu.dosw.rideci.application.mapper;

import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.NotificationEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Mapper de la capa de aplicación para construir InAppNotification
 * a partir de eventos de dominio (NotificationEvent).
 */
public final class NotificationApplicationMapper {

    private NotificationApplicationMapper() {

    }

    /**
     * Construye una InAppNotification a partir de un NotificationEvent más
     * un título y mensaje ya “resueltos” (por ejemplo, plantillas).
     *
     * @param event   evento de dominio que dispara la notificación
     * @param title   título a mostrar en la notificación
     * @param message mensaje a mostrar en la notificación
     * @return InAppNotification lista para pasar al caso de uso CreateNotificationUseCase
     */
    public static InAppNotification fromEvent(NotificationEvent event,
                                              String title,
                                              String message) {

        UUID userId = event.getUserId();

        return InAppNotification.builder()
                .notificationId(null)
                .userId(userId)
                .title(title)
                .message(message)
                .eventType(event.getEventType())
                .priority(event.getPriority())
                .status(NotificationStatus.UNREAD)
                .createdAt(OffsetDateTime.now())
                .expiresAt(null)
                .build();
    }
}
