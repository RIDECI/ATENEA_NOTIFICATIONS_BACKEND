package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.AppNotification;
import java.util.List;

public interface GetRecentNotificationsUseCase {
    List<AppNotification> getRecentNotifications(GetRecentNotificationsCommand command);

    record GetRecentNotificationsCommand(
            int limit,
            String userId
    ) {}
}