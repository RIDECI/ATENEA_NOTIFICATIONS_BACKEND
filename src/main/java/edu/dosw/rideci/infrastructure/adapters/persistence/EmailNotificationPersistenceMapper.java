package edu.dosw.rideci.infrastructure.adapters.persistence;

import edu.dosw.rideci.domain.model.EmailNotification;
import edu.dosw.rideci.infrastructure.persistance.Entity.EmailNotificationEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class EmailNotificationPersistenceMapper {
    public EmailNotificationEntity toEntity(EmailNotification domain) {
        if (domain == null) return null;
        return EmailNotificationEntity.builder()
                .id(domain.getId())
                .userId(domain.getUser() != null ? domain.getUser().getId() : null)
                .emailType(domain.getEmailType())
                .content(domain.getContent())
                .timestamp(domain.getTimestamp())
                .sendStatus(domain.getSendStatus())
                .error(domain.getError())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    public EmailNotification toDomain(EmailNotificationEntity entity) {
        if (entity == null) return null;
        return EmailNotification.builder()
                .id(entity.getId())
                .emailType(entity.getEmailType())
                .content(entity.getContent())
                .timestamp(entity.getTimestamp())
                .sendStatus(entity.getSendStatus())
                .error(entity.getError())
                .build();
    }
    public EmailNotificationEntity updateEntityFromDomain(EmailNotificationEntity entity, EmailNotification domain) {
        if (entity == null || domain == null) return entity;
        entity.setEmailType(domain.getEmailType());
        entity.setContent(domain.getContent());
        entity.setTimestamp(domain.getTimestamp());
        entity.setSendStatus(domain.getSendStatus());
        entity.setError(domain.getError());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
