package edu.dosw.rideci.infrastructure.controller.dto;

import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreateNotificationRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.NotificationResponse;

import java.time.OffsetDateTime;
import java.util.UUID;

public class NotificationDtoMapper {

    private NotificationDtoMapper() {
    }

    public static InAppNotification toDomain(CreateNotificationRequest request) {
        return InAppNotification.builder()
                .notificationId(null)
                .userId(request.getUserId())
                .title(request.getTitle())
                .message(request.getMessage())
                .eventType(request.getEventType())
                .priority(request.getPriority())
                .createdAt(OffsetDateTime.now())
                .build();
    }

    public static NotificationResponse toResponse(InAppNotification notification) {
        UUID id = notification.getNotificationId();

        return NotificationResponse.builder()
                .notificationId(id)
                .userId(notification.getUserId())
                .eventType(notification.getEventType())
                .channel(null)
                .title(notification.getTitle())
                .message(notification.getMessage())
                .priority(notification.getPriority())
                .metadataJson(null)
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .expiresAt(notification.getExpiresAt())
                .build();
    }
}
