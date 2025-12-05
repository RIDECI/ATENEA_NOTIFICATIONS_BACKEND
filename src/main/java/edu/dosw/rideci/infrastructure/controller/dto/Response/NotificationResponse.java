package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.Enum.EmailSendStatus;
import edu.dosw.rideci.domain.model.Enum.MessageType;
import edu.dosw.rideci.domain.model.Enum.NotificationChannel;
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
    private MessageType eventType;
    private NotificationChannel channel;
    private String title;
    private String message;
    private String priority;
    private String metadataJson;
    private EmailSendStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime readAt;
    private OffsetDateTime expiresAt;
}
