package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.NotificationEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationSubscriber implements NotificationSubscriber {

    private final EventBus eventBus;
    private final EmailNotificationSender emailNotificationSender;
    private final UserEmailResolver userEmailResolver;

    private final String handlerId = "email-notification-subscriber";
    private final boolean active = true;

    @PostConstruct
    public void register() {
        if (!active) {
            return;
        }
        getSubscribedEvents().forEach(type -> eventBus.subscribe(type, this));
        log.info("EmailNotificationSubscriber suscrito a eventos: {}", getSubscribedEvents());
    }

    @Override
    public void handleEvent(NotificationEvent event) {
        try {
            InAppNotification notification = event.getNotification();

            if (notification == null) {
                log.warn("Evento {} sin InAppNotification, se omite envío de email", event.getEventType());
                return;
            }

            UUID userId = notification.getUserId();
            if (userId == null) {
                log.warn("Notificación sin userId, no se puede resolver email. notificationId={}",
                        notification.getNotificationId());
                return;
            }

            String email = userEmailResolver.resolveEmail(userId.toString());

            if (email != null && !email.isBlank()) {
                emailNotificationSender.sendNotification(notification, email);
                log.info(
                        "Notificación por correo enviada a {} (userId={}, notificationId={})",
                        email, userId, notification.getNotificationId()
                );
            } else {
                log.warn("No se encontró correo electrónico para userId={}", userId);
            }
        } catch (Exception e) {
            log.error("Error procesando envío de email para evento {}: {}", event.getEventType(), e.getMessage(), e);
        }
    }

    @Override
    public List<NotificationType> getSubscribedEvents() {
        return List.of(NotificationType.NOTIFICATION_CREATED);
    }

    @Override
    public String getName() {
        return handlerId;
    }
}
