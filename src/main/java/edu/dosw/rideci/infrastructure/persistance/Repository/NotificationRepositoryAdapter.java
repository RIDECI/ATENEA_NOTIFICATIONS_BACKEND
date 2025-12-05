package edu.dosw.rideci.infrastructure.persistance.Repository;

import edu.dosw.rideci.application.port.out.NotificationRepositoryPort;
import edu.dosw.rideci.infrastructure.persistance.Entity.NotificationEntity;
import edu.dosw.rideci.infrastructure.persistance.Repository.mapper.NotificationPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementación del puerto de repositorio usando
 * almacenamiento en memoria (sin base de datos).
 */
@Component
public class NotificationRepositoryAdapter implements NotificationRepositoryPort {

    // Almacén en memoria: id -> entidad
    private final Map<UUID, NotificationEntity> storage = new ConcurrentHashMap<>();

    @Override
    public InAppNotification save(InAppNotification notification) {
        if (notification.getNotificationId() == null) {
            notification.setNotificationId(UUID.randomUUID());
        }

        NotificationEntity entity = NotificationPersistenceMapper.toEntity(notification);
        storage.put(entity.getNotificationId(), entity);

        return NotificationPersistenceMapper.toDomain(entity);
    }

    @Override
    public Optional<InAppNotification> findById(UUID id) {
        NotificationEntity entity = storage.get(id);
        return Optional.ofNullable(NotificationPersistenceMapper.toDomain(entity));
    }

    @Override
    public List<InAppNotification> findByUserIdOrderByCreatedAtDesc(UUID userId) {
        return storage.values().stream()
                .filter(e -> userId.equals(e.getUserId()))
                .sorted(Comparator.comparing(NotificationEntity::getCreatedAt).reversed())
                .map(NotificationPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
