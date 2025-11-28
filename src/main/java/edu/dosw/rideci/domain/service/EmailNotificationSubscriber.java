package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.NotificationEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationSubscriber implements NotificationSubscriber {

    private final EventBus eventBus;
    private final EmailNotificationSender emailNotificationSender;
    private final UserEmailResolver userEmailResolver;

    @PostConstruct
    public void init() {
        eventBus.subscribe(EventType.NOTIFICATION_CREATED, this);
        log.info("EmailNotificationSubscriber suscrito a NOTIFICATION_CREATED");
    }

    @Override
    public void handleEvent(NotificationEvent event) {
        try {
            InAppNotification notification = event.getNotification();
            String userId = notification.getUserId() != null ? notification.getUserId().toString() : null;

            String email = userEmailResolver.resolveEmail(userId);

            if (email != null && !email.isBlank()) {
                emailNotificationSender.sendNotification(notification, email);
                log.info("Notificación por correo enviada a {} (user {})", email, userId);
            } else {
                log.warn("No se encontró correo electrónico para user {}", userId);
            }
        } catch (Exception e) {
            log.error("Error procesando envío de email: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<EventType> getSubscribedEvents() {
        return List.of(EventType.NOTIFICATION_CREATED);
    }

    @Override
    public String getName() {
        return "EmailNotificationSubscriber";
    }
}
