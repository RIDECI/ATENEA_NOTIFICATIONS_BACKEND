package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InAppNotification {

    private UUID notificationId;
    private UUID userId;
    private String title;
    private String message;
    private EventType eventType;
    /**
     * Prioridad como texto: "LOW", "NORMAL", "HIGH"
     */
    private String priority;
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
