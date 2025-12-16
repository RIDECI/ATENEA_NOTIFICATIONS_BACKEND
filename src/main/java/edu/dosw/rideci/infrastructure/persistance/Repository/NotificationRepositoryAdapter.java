package edu.dosw.rideci.infrastructure.persistance.Repository;

import edu.dosw.rideci.application.port.out.NotificationRepositoryPort;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.infrastructure.persistance.Entity.NotificationEntity;
import edu.dosw.rideci.infrastructure.persistance.Repository.mapper.NotificationPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Adaptador de repositorio para notificaciones usando almacenamiento en memoria.
 *
 * Implementa el puerto {@link NotificationRepositoryPort} manteniendo un
 * almacén en memoria basado en {@link ConcurrentHashMap}, sin depender
 * de una base de datos real. Es útil para pruebas, desarrollo temprano
 * o entornos donde aún no se ha configurado infraestructura de persistencia.
 *
 * @author RideECI
 * @version 1.0
 */
@Component
public class NotificationRepositoryAdapter implements NotificationRepositoryPort {

    /** Almacén en memoria: id de notificación -> entidad de persistencia. */
    private final Map<UUID, NotificationEntity> storage = new ConcurrentHashMap<>();

    /**
     * Guarda una notificación en el almacén en memoria.
     * Si la notificación no tiene identificador, se genera un {@link UUID} nuevo.
     *
     * @param notification Notificación de dominio a persistir.
     * @return Notificación de dominio almacenada, reconstruida desde la entidad.
     */
    @Override
    public InAppNotification save(InAppNotification notification) {
        if (notification.getNotificationId() == null) {
            notification.setNotificationId(UUID.randomUUID());
        }

        NotificationEntity entity = NotificationPersistenceMapper.toEntity(notification);
        storage.put(entity.getNotificationId(), entity);

        return NotificationPersistenceMapper.toDomain(entity);
    }

    /**
     * Busca una notificación por su identificador en el almacén en memoria.
     *
     * @param id Identificador único de la notificación.
     * @return {@link Optional} que contiene la notificación si existe,
     *         o vacío si no se encuentra.
     */
    @Override
    public Optional<InAppNotification> findById(UUID id) {
        NotificationEntity entity = storage.get(id);
        return Optional.ofNullable(NotificationPersistenceMapper.toDomain(entity));
    }

    /**
     * Obtiene todas las notificaciones asociadas a un usuario, ordenadas por
     * fecha de creación descendente (de la más reciente a la más antigua).
     *
     * @param userId Identificador del usuario.
     * @return Lista de notificaciones del usuario ordenadas por {@code createdAt} descendente.
     */
    @Override
    public List<InAppNotification> findByUserIdOrderByCreatedAtDesc(UUID userId) {
        return storage.values().stream()
                .filter(e -> userId.equals(e.getUserId()))
                .sorted(Comparator.comparing(NotificationEntity::getCreatedAt).reversed())
                .map(NotificationPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
