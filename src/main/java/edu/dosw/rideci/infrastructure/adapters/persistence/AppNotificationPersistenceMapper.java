package edu.dosw.rideci.infrastructure.adapters.persistence;

import edu.dosw.rideci.domain.model.AppNotification;
import edu.dosw.rideci.infrastructure.persistance.Entity.AppNotificationEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class AppNotificationPersistenceMapper {
    public AppNotificationEntity toEntity(AppNotification domain) {
        if (domain == null) return null;
        return AppNotificationEntity.builder()
                .id(domain.getId())
                .userId(domain.getUser() != null ? domain.getUser().getId() : null)
                .messageType(domain.getMessageType())
                .message(domain.getMessage())
                .timestamp(domain.getTimestamp())
                .category(domain.getCategory())
                .read(domain.isRead())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    public AppNotification toDomain(AppNotificationEntity entity) {
        if (entity == null) return null;
        return AppNotification.builder()
                .id(entity.getId())
                .messageType(entity.getMessageType())
                .message(entity.getMessage())
                .timestamp(entity.getTimestamp())
                .category(entity.getCategory())
                .read(entity.isRead())
                .build();
    }
    public AppNotificationEntity updateEntityFromDomain(AppNotificationEntity entity, AppNotification domain) {
        if (entity == null || domain == null) return entity;
        entity.setMessageType(domain.getMessageType());
        entity.setMessage(domain.getMessage());
        entity.setTimestamp(domain.getTimestamp());
        entity.setCategory(domain.getCategory());
        entity.setRead(domain.isRead());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}