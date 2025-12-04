package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.AppNotification;
import edu.dosw.rideci.domain.model.Enum.Category;
import java.time.LocalDateTime;
import java.util.List;

public interface FilterNotificationsUseCase {
    List<AppNotification> filterNotifications(FilterNotificationsCommand command);

    record FilterNotificationsCommand(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Category category,
            String userId
    ) {}
}