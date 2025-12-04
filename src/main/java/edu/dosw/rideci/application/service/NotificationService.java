package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.CreateNotificationUseCase;
import edu.dosw.rideci.application.port.in.GetUserNotificationsUseCase;
import edu.dosw.rideci.application.port.in.MarkNotificationAsReadUseCase;
import edu.dosw.rideci.application.port.out.NotificationRepositoryPort;
import edu.dosw.rideci.exceptions.NotificationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService implements
        CreateNotificationUseCase,
        GetUserNotificationsUseCase,
        MarkNotificationAsReadUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;
    private final NotificationDomainService domainService;

    @Override
    public InAppNotification createNotification(InAppNotification notification) {
        domainService.initializeNotification(notification);
        return notificationRepositoryPort.save(notification);
    }

    @Override
    public List<InAppNotification> getNotificationsByUserId(UUID userId) {
        return notificationRepositoryPort.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public InAppNotification markAsRead(UUID notificationId) {
        InAppNotification notification = notificationRepositoryPort.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(
                        "Notification not found with id: " + notificationId));

        domainService.markAsRead(notification);
        return notificationRepositoryPort.save(notification);
    }
}
