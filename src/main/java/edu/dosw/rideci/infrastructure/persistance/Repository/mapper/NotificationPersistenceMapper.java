package edu.dosw.rideci.infrastructure.persistance.Repository.mapper;

import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.infrastructure.persistance.Entity.NotificationEntity;

/**
 * Mapper entre el modelo de dominio y la entidad de persistencia de notificaciones.
 *
 * Aunque actualmente se utilice almacenamiento en memoria, este mapper mantiene
 * la separación de capas entre dominio e infraestructura, facilitando una futura
 * migración a una base de datos real sin impactar el modelo de dominio.
 *
 * Provee métodos estáticos para convertir:
 * - De {@link InAppNotification} a {@link NotificationEntity}.
 * - De {@link NotificationEntity} a {@link InAppNotification}.
 *
 * @author RideECI
 * @version 1.0
 */
public final class NotificationPersistenceMapper {

    /**
     * Constructor privado para impedir la instanciación de la clase utility.
     */
    private NotificationPersistenceMapper() {

    }

    /**
     * Convierte una notificación del dominio en su representación de persistencia.
     *
     * @param notification Notificación del dominio a convertir.
     * @return Entidad de persistencia {@link NotificationEntity} correspondiente,
     *         o {@code null} si {@code notification} es {@code null}.
     */
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

    /**
     * Convierte una entidad de persistencia de notificación en el modelo de dominio.
     *
     * @param entity Entidad {@link NotificationEntity} a convertir.
     * @return Instancia de {@link InAppNotification} correspondiente,
     *         o {@code null} si {@code entity} es {@code null}.
     */
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
