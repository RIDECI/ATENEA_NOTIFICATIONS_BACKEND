package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.Enum.NotificationChannel;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.Enum.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Notification {

    private UUID id;
    private UUID userId;
    private EventType type;
    private NotificationChannel channel;
    private String title;
    private String message;
    private String metadataJson;
    private NotificationStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime sentAt;
    private OffsetDateTime readAt;
}
