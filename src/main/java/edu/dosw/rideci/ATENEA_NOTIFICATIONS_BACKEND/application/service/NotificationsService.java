package edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.application.service;

import edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.application.port.in.NotificationCommandUseCase;
import edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.application.port.in.NotificationQueryUseCase;
import edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.application.port.out.NotificationRepositoryPort;
import edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.domain.model.InAppNotification;
import edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.domain.model.Enum.NotificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationsService
        implements NotificationCommandUseCase, NotificationQueryUseCase {

    private final NotificationRepositoryPort repository;

    @Override
    public InAppNotification createNotification(InAppNotification notification) {

   
        notification.setCreatedAt(LocalDateTime.now());

        if (notification.getStatus() == null) {
            notification.setStatus(NotificationStatus.UNREAD);
        }

        return repository.save(notification);
    }

    @Override
    public boolean markAsRead(String notificationId) {
        return repository.findById(notificationId)
                .map(n -> {
                    n.markAsRead();
                    repository.save(n);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<InAppNotification> getUserNotifications(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public void clearExpiredNotifications() {
        repository.deleteAllExpired();
    }
}
