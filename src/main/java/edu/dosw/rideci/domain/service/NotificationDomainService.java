package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.InAppNotification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class NotificationDomainService {

    public void initializeNotification(InAppNotification notification) {
        notification.setStatus(NotificationStatus.UNREAD);
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(OffsetDateTime.now());
        }
    }

    public void markAsRead(InAppNotification notification) {
        notification.setStatus(NotificationStatus.READ);
        notification.setReadAt(OffsetDateTime.now());
    }

    public void archive(InAppNotification notification) {
        notification.setStatus(NotificationStatus.ARCHIVED);
    }

    public void expire(InAppNotification notification) {
        notification.setStatus(NotificationStatus.EXPIRED);
        notification.setExpiresAt(OffsetDateTime.now());
    }

    public boolean isExpired(InAppNotification notification) {
        return notification.isExpired();
    }
}
