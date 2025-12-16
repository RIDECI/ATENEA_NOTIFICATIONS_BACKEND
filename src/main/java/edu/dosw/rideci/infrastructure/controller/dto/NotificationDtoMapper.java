package edu.dosw.rideci.infrastructure.controller.dto;

import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreateNotificationRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.NotificationResponse;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Mapper para convertir entre DTOs de notificación y el modelo de dominio.
 *
 * Provee métodos estáticos para:
 * - Construir una {@link InAppNotification} a partir de un {@link CreateNotificationRequest}.
 * - Construir un {@link NotificationResponse} a partir de una {@link InAppNotification}.
 *
 * @author RideECI
 * @version 1.0
 */
public class NotificationDtoMapper {

    /**
     * Constructor privado para impedir la instanciación de la clase utility.
     */
    private NotificationDtoMapper() {
    }

    /**
     * Convierte un {@link CreateNotificationRequest} en una {@link InAppNotification}
     * del dominio.
     *
     * Inicializa la notificación con:
     * - {@code notificationId} en {@code null} (será asignado posteriormente).
     * - {@code userId}, {@code title}, {@code message}, {@code eventType} y {@code priority}
     *   a partir del request.
     * - {@code createdAt} con la fecha y hora actual.
     *
     * @param request DTO de creación de notificación.
     * @return Instancia de {@link InAppNotification} construida a partir del request.
     */
    public static InAppNotification toDomain(CreateNotificationRequest request) {
        return InAppNotification.builder()
                .notificationId(null)
                .userId(request.getUserId())
                .title(request.getTitle())
                .message(request.getMessage())
                .eventType(request.getEventType())
                .priority(request.getPriority())
                .createdAt(OffsetDateTime.now())
                .build();
    }

    /**
     * Convierte una {@link InAppNotification} en un {@link NotificationResponse}
     * para exponerla a través de la API.
     *
     * Por ahora, los campos {@code channel} y {@code metadataJson} se dejan en {@code null},
     * ya que no están modelados directamente en {@link InAppNotification}.
     *
     * @param notification Notificación del dominio.
     * @return DTO de respuesta con la información de la notificación.
     */
    public static NotificationResponse toResponse(InAppNotification notification) {
        UUID id = notification.getNotificationId();

        return NotificationResponse.builder()
                .notificationId(id)
                .userId(notification.getUserId())
                .eventType(notification.getEventType())
                .channel(null)
                .title(notification.getTitle())
                .message(notification.getMessage())
                .priority(notification.getPriority())
                .metadataJson(null)
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .expiresAt(notification.getExpiresAt())
                .build();
    }
}
