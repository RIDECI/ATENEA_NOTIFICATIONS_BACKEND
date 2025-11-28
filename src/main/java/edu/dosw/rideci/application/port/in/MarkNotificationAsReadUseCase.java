package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.InAppNotification;

import java.util.UUID;

public interface MarkNotificationAsReadUseCase {

    InAppNotification markAsRead(UUID notificationId);
}
