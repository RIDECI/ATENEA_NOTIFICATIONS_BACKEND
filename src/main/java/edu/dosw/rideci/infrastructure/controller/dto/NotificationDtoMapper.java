package edu.dosw.rideci.infrastructure.controller.dto;

import edu.dosw.rideci.domain.model.AppNotification;
import edu.dosw.rideci.domain.model.Enum.EmailSendStatus;
import edu.dosw.rideci.domain.model.User;
import edu.dosw.rideci.domain.model.Enum.NotificationChannel;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreateNotificationRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.NotificationResponse;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class NotificationDtoMapper {

    private NotificationDtoMapper() {
    }


    /**
     * Mapea una AppNotification de dominio a NotificationResponse (DTO de salida).
     */
    public static NotificationResponse toResponse(AppNotification notification) {
        // Convertimos LocalDateTime → OffsetDateTime (NotificationResponse usa OffsetDateTime)
        OffsetDateTime createdAt = notification.getTimestamp() != null
                ? notification.getTimestamp().atOffset(ZoneOffset.UTC)
                : null;

        // Estado de la notificación según si está leída o no
        EmailSendStatus status = notification.isRead()
                ? EmailSendStatus.READ
                : EmailSendStatus.SENT;

        return NotificationResponse.builder()
                .notificationId(notification.getId() != null
                        ? UUID.fromString(notification.getId())
                        : null)

                .userId(notification.getUser() != null && notification.getUser().getId() != null
                        ? UUID.fromString(notification.getUser().getId())
                        : null)

                .eventType(notification.getMessageType())
                .channel(NotificationChannel.IN_APP)
                .title(notification.getSummary())
                .message(notification.getMessage())
                .priority(null)
                .metadataJson(null)
                .status(status)
                .createdAt(createdAt)
                .readAt(notification.isRead() ? createdAt : null)
                .expiresAt(null)
                .build();
    }
}
