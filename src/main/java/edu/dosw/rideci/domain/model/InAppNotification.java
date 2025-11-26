package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class InAppNotification {

    private UUID notificationId;
    private UUID userId;
    private String title;
    private String message;
    private EventType eventType;
    private String priority; // "LOW", "NORMAL", "HIGH"
    private NotificationStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime readAt;
    private OffsetDateTime expiresAt;

    public void markAsRead() {
        this.status = NotificationStatus.READ;
        this.readAt = OffsetDateTime.now();
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(OffsetDateTime.now());
    }

    public String getDisplayMessage() {
        return title + " - " + message;
    }
}
