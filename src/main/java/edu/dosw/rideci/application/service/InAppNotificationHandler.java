package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.mapper.NotificationApplicationMapper;
import edu.dosw.rideci.application.port.in.CreateNotificationUseCase;
import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.NotificationEvent;
import edu.dosw.rideci.domain.service.EventBus;
import edu.dosw.rideci.domain.service.NotificationSubscriber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Manejador de notificaciones in-app basado en eventos de dominio.
 * Se suscribe al {@link EventBus} y crea notificaciones internas para ciertos tipos de eventos.
 *
 * @author RideECI
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class InAppNotificationHandler implements NotificationSubscriber {

    private final EventBus eventBus;
    private final CreateNotificationUseCase createNotificationUseCase;
    private final NotificationApplicationMapper notificationApplicationMapper;

    private final String handlerId = "in-app-notification-handler";
    private final boolean isActive = true;

    /**
     * Registra este manejador en el {@link EventBus} para los eventos soportados,
     * siempre y cuando el manejador esté activo.
     */
    @PostConstruct
    public void register() {
        if (!isActive) {
            return;
        }
        getSubscribedEvents().forEach(e -> eventBus.subscribe(e, this));
    }

    /**
     * Procesa un evento de notificación recibido.
     * Construye un título y mensaje amigables, mapea el evento a una
     * {@link InAppNotification} y delega su creación al caso de uso.
     *
     * @param event Evento de dominio que origina la notificación.
     */
    @Override
    public void handleEvent(NotificationEvent event) {
        String title = buildTitle(event);
        String message = buildMessage(event);

        InAppNotification notification =
                notificationApplicationMapper.fromEvent(event, title, message);

        createNotificationUseCase.createNotification(notification);
    }

    /**
     * Define la lista de tipos de evento a los que este manejador se suscribe.
     *
     * @return Lista de tipos de {@link EventType} soportados por este manejador.
     */
    @Override
    public List<EventType> getSubscribedEvents() {
        return List.of(
                EventType.TRIP_CREATED,
                EventType.TRIP_CANCELLED,
                EventType.TRIP_COMPLETED,
                EventType.PAYMENT_CONFIRMED,
                EventType.PAYMENT_FAILED,
                EventType.EMERGY_BOTON,
                EventType.LOCATION_ALERT,
                EventType.SECURITY_INCIDENT
        );
    }

    /**
     * Obtiene el identificador único de este manejador de notificaciones.
     *
     * @return Nombre del manejador.
     */
    @Override
    public String getName() {
        return handlerId;
    }

    /**
     * Construye el título de la notificación en función del tipo de evento.
     *
     * @param event Evento de notificación.
     * @return Título de la notificación en formato legible.
     */
    private String buildTitle(NotificationEvent event) {
        return switch (event.getEventType()) {
            case TRIP_CREATED -> "New trip created";
            case TRIP_CANCELLED -> "Trip cancelled";
            case TRIP_COMPLETED -> "Trip completed";
            case PAYMENT_FAILED -> "Payment failed";
            case PAYMENT_CONFIRMED -> "Payment confirmed";
            case EMERGY_BOTON -> "Emergency alert";
            case LOCATION_ALERT -> "Location alert";
            case SECURITY_INCIDENT -> "Security incident";
            default -> "Notification";
        };
    }

    /**
     * Construye el mensaje de la notificación a partir del contenido del evento.
     * Actualmente utiliza la representación JSON del evento.
     *
     * @param event Evento de notificación.
     * @return Mensaje de la notificación.
     */
    private String buildMessage(NotificationEvent event) {
        return event.toJSON();
    }
}
