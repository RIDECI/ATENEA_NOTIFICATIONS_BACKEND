package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.Enum.NotificationChannel;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class NotificationResponse {

    private UUID notificationId;
    private UUID userId;
    private EventType eventType;
    private NotificationChannel channel;
    private String title;
    private String message;
    private String priority;
    private String metadataJson;
    private NotificationStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime readAt;
    private OffsetDateTime expiresAt;
}
