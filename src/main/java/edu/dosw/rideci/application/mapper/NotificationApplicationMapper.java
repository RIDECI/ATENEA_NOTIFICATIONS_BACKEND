package edu.dosw.rideci.application.mapper;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.NotificationEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class NotificationApplicationMapper {

    /**
     * Mapea un NotificationEvent a un InAppNotification con valores por defecto.
     */
    public InAppNotification toInApp(NotificationEvent event) {
        if (event == null) {
            return null;
        }


        InAppNotification notification = new InAppNotification();


        if (event.getEventId() != null && !event.getEventId().isBlank()) {
            notification.setNotificationId(UUID.fromString(event.getEventId()));
        }
        if (event.getUserId() != null && !event.getUserId().isBlank()) {
            notification.setUserId(UUID.fromString(event.getUserId()));
        }

        notification.setTitle(buildTitle(event.getEventType()));
        notification.setMessage(event.getMessage());
        notification.setEventType(event.getEventType());


        notification.setPriority(String.valueOf(event.getPriority()));


        notification.setStatus(NotificationStatus.UNREAD);


        Instant ts = event.getTimestamp() != null ? event.getTimestamp() : Instant.now();
        OffsetDateTime createdAt = OffsetDateTime.ofInstant(ts, ZoneOffset.UTC);
        notification.setCreatedAt(createdAt);
        notification.setReadAt(null);
        notification.setExpiresAt(null);

        return notification;
    }

    /**
     * Sobrecarga usada por InAppNotificationHandler:
     * permite sobreescribir título y mensaje.
     */
    public InAppNotification fromEvent(NotificationEvent event,
                                       String titleOverride,
                                       String messageOverride) {
        InAppNotification notification = toInApp(event);
        if (notification == null) {
            return null;
        }

        if (titleOverride != null && !titleOverride.isBlank()) {
            notification.setTitle(titleOverride);
        }
        if (messageOverride != null && !messageOverride.isBlank()) {
            notification.setMessage(messageOverride);
        }

        return notification;
    }

    private String buildTitle(EventType type) {
        if (type == null) {
            return "Notification";
        }
        return switch (type) {
            case TRIP_CREATED -> "Nuevo viaje creado";
            case TRIP_CANCELLED -> "Viaje cancelado";
            case TRIP_COMPLETED -> "Viaje completado";
            case PAYMENT_CONFIRMED -> "Pago confirmado";
            case PAYMENT_FAILED -> "Error en el pago";
            case EMERGY_BOTON, SECURITY_INCIDENT -> "Alerta de seguridad";
            case LOCATION_ALERT -> "Alerta de ubicación";
            case DRIVER_VALIDATED -> "Conductor validado";
            case USER_REGISTERED -> "Bienvenido a RideECI";
            case NEW_DISTINTIVE -> "Nuevo distintivo asignado";
            case TRIP_UPDATED -> "Actualización de viaje";
            case RATING_SUBMITTED -> "Nueva calificación registrada";
            default -> "Notificación de RideECI";
        };
    }
}
