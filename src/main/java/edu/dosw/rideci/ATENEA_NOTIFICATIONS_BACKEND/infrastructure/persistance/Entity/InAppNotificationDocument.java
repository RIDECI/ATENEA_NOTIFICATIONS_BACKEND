package edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.infrastructure.persistence.Entity;

import edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.domain.model.Enum.NotificationStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "in_app_notifications")
public class InAppNotificationDocument {

    @Id
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

}
