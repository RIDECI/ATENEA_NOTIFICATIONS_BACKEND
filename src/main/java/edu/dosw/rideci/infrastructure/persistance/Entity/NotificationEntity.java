package edu.dosw.rideci.infrastructure.persistance.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {

    private UUID notificationId;
    private UUID userId;

    private String title;
    private String message;

    private EventType eventType;
    /**
     * Prioridad serializada como texto para desacoplar de enums concretos.
     */
    private String priority;

    private NotificationStatus status;

    private OffsetDateTime createdAt;
    private OffsetDateTime readAt;
    private OffsetDateTime expiresAt;
}
