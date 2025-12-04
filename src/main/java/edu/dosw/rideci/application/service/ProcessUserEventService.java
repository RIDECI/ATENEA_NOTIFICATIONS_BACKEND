package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.events.listener.UserSyncFailedEvent;
import edu.dosw.rideci.application.port.out.AppNotificationRepositoryPort;
import edu.dosw.rideci.application.port.out.EmailNotificationPort;
import edu.dosw.rideci.domain.model.AppNotification;
import edu.dosw.rideci.domain.model.Enum.Category;
import edu.dosw.rideci.domain.model.Enum.MessageType;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ProcessUserEventService {

    private final AppNotificationRepositoryPort appNotificationRepositoryPort;
    private final EmailNotificationPort emailNotificationPort;

    public ProcessUserEventService(AppNotificationRepositoryPort appNotificationRepositoryPort, EmailNotificationPort emailNotificationPort) {
        this.appNotificationRepositoryPort = appNotificationRepositoryPort;
        this.emailNotificationPort = emailNotificationPort;
    }

    public void processUserSyncFailed(UserSyncFailedEvent event) {
        AppNotification notification = AppNotification.builder()
                .user(null)
                .messageType(MessageType.SECURITY_ALERT)
                .message("Error en sincronización de usuario: " + event.getEmail() + " - " + event.getReason())
                .timestamp(LocalDateTime.now())
                .category(Category.ALERTS)
                .read(false)
                .build();

        appNotificationRepositoryPort.save(notification);
    }
}