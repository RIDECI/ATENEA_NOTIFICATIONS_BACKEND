package edu.dosw.rideci.infrastructure.persistance.Repository.mapper;

import edu.dosw.rideci.domain.model.User;
import edu.dosw.rideci.infrastructure.persistance.Entity.UserEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User domain) {
        if (domain == null) return null;

        return UserEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .email(domain.getEmail())
                .profile(domain.getProfile())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .profile(entity.getProfile())
                .build();
    }

    public UserEntity updateEntityFromDomain(UserEntity entity, User domain) {
        if (entity == null || domain == null) return entity;

        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setProfile(domain.getProfile());
        entity.setUpdatedAt(LocalDateTime.now());

        return entity;
    }
}
