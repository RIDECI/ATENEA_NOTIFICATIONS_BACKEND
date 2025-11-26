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

@Component
@RequiredArgsConstructor
public class InAppNotificationHandler implements NotificationSubscriber {

    private final EventBus eventBus;
    private final CreateNotificationUseCase createNotificationUseCase;

    private final String handlerId = "in-app-notification-handler";
    private final boolean isActive = true;

    @PostConstruct
    public void register() {
        if (!isActive) {
            return;
        }
        getSubscribedEvents().forEach(e -> eventBus.subscribe(e, this));
    }

    @Override
    public void handleEvent(NotificationEvent event) {
        String title = buildTitle(event);
        String message = buildMessage(event);

        InAppNotification notification =
                NotificationApplicationMapper.fromEvent(event, title, message);

        createNotificationUseCase.createNotification(notification);
    }

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

    @Override
    public String getName() {
        return handlerId;
    }

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

    private String buildMessage(NotificationEvent event) {

        return event.toJSON();
    }
}
