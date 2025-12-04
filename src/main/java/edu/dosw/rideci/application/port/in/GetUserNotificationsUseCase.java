package edu.dosw.rideci.application.port.in;

import java.util.List;
import java.util.UUID;

public interface GetUserNotificationsUseCase {

    List<InAppNotification> getNotificationsByUserId(UUID userId);
}
