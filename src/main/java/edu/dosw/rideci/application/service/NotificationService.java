package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.CreateNotificationUseCase;
import edu.dosw.rideci.application.port.in.GetUserNotificationsUseCase;
import edu.dosw.rideci.application.port.in.MarkNotificationAsReadUseCase;
import edu.dosw.rideci.application.port.out.NotificationRepositoryPort;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.NotificationEvent;
import edu.dosw.rideci.domain.service.EventBus;
import edu.dosw.rideci.domain.service.NotificationDomainService;
import edu.dosw.rideci.exceptions.NotificationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de aplicación para la gestión de notificaciones in-app.
 * Implementa los casos de uso de creación, consulta por usuario y marcación como leída.
 *
 * Coordina la lógica de dominio ({@link NotificationDomainService}) con el
 * puerto de persistencia ({@link NotificationRepositoryPort}) y la publicación
 * de eventos de dominio mediante {@link EventBus}.
 *
 * @author RideECI
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class NotificationService implements
        CreateNotificationUseCase,
        GetUserNotificationsUseCase,
        MarkNotificationAsReadUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;
    private final NotificationDomainService domainService;
    private final EventBus eventBus;

    /**
     * Crea una nueva notificación in-app.
     * Inicializa la notificación mediante el servicio de dominio,
     * la persiste en el repositorio y publica un evento de dominio
     * para que otros componentes (como el suscriptor de email)
     * puedan reaccionar.
     *
     * @param notification Notificación a crear.
     * @return Notificación creada y persistida.
     */
    @Override
    public InAppNotification createNotification(InAppNotification notification) {
        domainService.initializeNotification(notification);
        InAppNotification saved = notificationRepositoryPort.save(notification);
        NotificationEvent event = NotificationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(NotificationType.NOTIFICATION_CREATED)
                .sourceModule("ATENEA_NOTIFICATIONS_BACKEND")
                .userId(saved.getUserId() != null ? saved.getUserId().toString() : null)
                .message(saved.getMessage())
                .priority(1)
                .timestamp(Instant.now())
                .notification(saved)
                .build();

        eventBus.publish(event);

        return saved;
    }

    /**
     * Obtiene las notificaciones de un usuario ordenadas por fecha de creación
     * descendente (de la más reciente a la más antigua).
     *
     * @param userId Identificador del usuario.
     * @return Lista de notificaciones asociadas al usuario.
     */
    @Override
    public List<InAppNotification> getNotificationsByUserId(UUID userId) {
        return notificationRepositoryPort.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Marca una notificación como leída.
     * Si la notificación no existe, lanza {@link NotificationNotFoundException}.
     *
     * @param notificationId Identificador de la notificación.
     * @return Notificación actualizada con el estado de lectura aplicado.
     */
    @Override
    public InAppNotification markAsRead(UUID notificationId) {
        InAppNotification notification = notificationRepositoryPort.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(
                        "Notification not found with id: " + notificationId));

        domainService.markAsRead(notification);
        return notificationRepositoryPort.save(notification);
    }
}
