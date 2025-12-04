package edu.dosw.rideci.infrastructure.persistance.Repository.mapper;

import edu.dosw.rideci.infrastructure.persistance.Entity.NotificationEntity;

/**
 * Mapper entre el modelo de dominio y la "entidad" de persistencia.
 * Aunque actualmente usamos almacenamiento en memoria, este mapper
 * permite mantener la separación de capas.
 */
public final class NotificationPersistenceMapper {

    private NotificationPersistenceMapper() {
        // Utility class
    }

    public static NotificationEntity toEntity(InAppNotification notification) {
        if (notification == null) {
            return null;
        }

        return NotificationEntity.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .eventType(notification.getEventType())
                .priority(notification.getPriority() != null
                        ? notification.getPriority().toString()
                        : null)
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .expiresAt(notification.getExpiresAt())
                .build();
    }

    public static InAppNotification toDomain(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }

        return InAppNotification.builder()
                .notificationId(entity.getNotificationId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .eventType(entity.getEventType())

                .priority(entity.getPriority())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .readAt(entity.getReadAt())
                .expiresAt(entity.getExpiresAt())
                .build();
    }
}
