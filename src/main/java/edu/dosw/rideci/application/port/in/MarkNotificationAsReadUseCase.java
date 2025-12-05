package edu.dosw.rideci.application.port.in;

import java.util.UUID;

public interface MarkNotificationAsReadUseCase {

    InAppNotification markAsRead(UUID notificationId);
}
