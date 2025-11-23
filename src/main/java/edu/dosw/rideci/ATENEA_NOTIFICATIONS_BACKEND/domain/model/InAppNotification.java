package edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.domain.model;

import edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.domain.model.Enum.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InAppNotification {

    private String notificationId;
    private String userId;
    private String priority; 
    private String title;
    private String message;

    private NotificationStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime readAt;

    private String eventType;     
    private String sourceModule;  
    private Map<String, Object> payload;

    public void markAsRead() {
        if (this.status == NotificationStatus.READ) {
            return;
        }
        this.status = NotificationStatus.READ;
        this.readAt = LocalDateTime.now();
    }
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public void markAsExpired() {
        if (isExpired()) {
            this.status = NotificationStatus.EXPIRED;
        }
    }
    public boolean canBeDisplayed() {
        return !isExpired()
                && status != NotificationStatus.ARCHIVED
                && status != NotificationStatus.EXPIRED;
    }
}
