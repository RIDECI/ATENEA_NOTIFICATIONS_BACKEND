package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.FilterNotificationsUseCase;
import edu.dosw.rideci.application.port.out.AppNotificationRepositoryPort;
import edu.dosw.rideci.domain.model.AppNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterNotificationsService implements FilterNotificationsUseCase {

    private final AppNotificationRepositoryPort appNotificationRepositoryPort;

    @Autowired
    public FilterNotificationsService(AppNotificationRepositoryPort appNotificationRepositoryPort) {
        this.appNotificationRepositoryPort = appNotificationRepositoryPort;
    }

    @Override
    public List<AppNotification> filterNotifications(FilterNotificationsCommand command) {
        LocalDateTime startDate = command.startDate();
        LocalDateTime endDate = command.endDate();
        String userId = command.userId();

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        List<AppNotification> filteredNotifications;

        if (userId != null && command.category() != null) {
            filteredNotifications = appNotificationRepositoryPort
                    .findByUserIdAndCategory(userId, command.category());
            return filteredNotifications.stream()
                    .filter(notification -> !notification.getTimestamp().isBefore(startDate)
                            && !notification.getTimestamp().isAfter(endDate))
                    .collect(Collectors.toList());
        } else if (userId != null) {
            filteredNotifications = appNotificationRepositoryPort
                    .findByUserIdAndTimestampBetween(userId, startDate, endDate);
        } else if (command.category() != null) {
            filteredNotifications = appNotificationRepositoryPort
                    .findByCategoryAndTimestampBetween(command.category(), startDate, endDate);
        } else {
            filteredNotifications = appNotificationRepositoryPort
                    .findByTimestampBetween(startDate, endDate);
        }

        return filteredNotifications;
    }
}