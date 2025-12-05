package edu.dosw.rideci.infrastructure.persistance.Repository.mapper;

import edu.dosw.rideci.domain.model.EmailNotification;
import edu.dosw.rideci.infrastructure.persistance.Entity.EmailNotificationEntity;
import edu.dosw.rideci.infrastructure.persistance.Entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailNotificationPersistenceMapper {

    public EmailNotificationEntity toEntity(EmailNotification domain) {
        if (domain == null) {
            return null;
        }

        UserEntity userEntity = null;
        if (domain.getUser() != null && domain.getUser().getId() != null) {
            userEntity = UserEntity.builder()
                    .id(domain.getUser().getId())
                    .build();
        }

        LocalDateTime ts = domain.getTimestamp() != null
                ? domain.getTimestamp()
                : LocalDateTime.now();

        return EmailNotificationEntity.builder()
                .id(domain.getId())
                .user(userEntity)
                .emailType(domain.getEmailType())
                .subject(domain.getSubject())
                .emailBody(domain.getEmailBody())
                .timestamp(ts)
                .sendStatus(domain.getSendStatus())
                .error(domain.getError())
                .build();
    }

    public EmailNotification toDomain(EmailNotificationEntity entity) {
        if (entity == null) {
            return null;
        }

        return EmailNotification.builder()
                .id(entity.getId())

                .emailType(entity.getEmailType())
                .subject(entity.getSubject())
                .emailBody(entity.getEmailBody())
                .timestamp(entity.getTimestamp())
                .sendStatus(entity.getSendStatus())
                .error(entity.getError())
                .build();
    }

    public EmailNotificationEntity updateEntityFromDomain(
            EmailNotificationEntity entity,
            EmailNotification domain
    ) {
        if (entity == null || domain == null) {
            return entity;
        }

        UserEntity userEntity = null;
        if (domain.getUser() != null && domain.getUser().getId() != null) {
            userEntity = UserEntity.builder()
                    .id(domain.getUser().getId())
                    .build();
        }

        entity.setUser(userEntity);
        entity.setEmailType(domain.getEmailType());
        entity.setSubject(domain.getSubject());
        entity.setTimestamp(domain.getTimestamp());
        entity.setSendStatus(domain.getSendStatus());
        entity.setError(domain.getError());

        return entity;
    }
}
