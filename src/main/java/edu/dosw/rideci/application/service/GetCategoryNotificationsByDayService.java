package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.GetCategoryNotificationsByDayUseCase;
import edu.dosw.rideci.application.port.out.AppNotificationRepositoryPort;
import edu.dosw.rideci.domain.model.AppNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetCategoryNotificationsByDayService implements GetCategoryNotificationsByDayUseCase {

    private final AppNotificationRepositoryPort appNotificationRepositoryPort;

    @Autowired
    public GetCategoryNotificationsByDayService(AppNotificationRepositoryPort appNotificationRepositoryPort) {
        this.appNotificationRepositoryPort = appNotificationRepositoryPort;
    }

    @Override
    public List<AppNotification> getCategoryNotificationsByDay(GetCategoryNotificationsByDayCommand command) {
        LocalDate date = command.date();
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<AppNotification> filteredNotifications;

        if (command.userId() != null && !command.userId().isEmpty()) {
            filteredNotifications = appNotificationRepositoryPort
                    .findByUserIdAndCategory(command.userId(), command.category());
        } else {
            filteredNotifications = appNotificationRepositoryPort
                    .findByCategory(command.category());
        }

        return filteredNotifications.stream()
                .filter(notification -> !notification.getTimestamp().isBefore(startOfDay)
                        && !notification.getTimestamp().isAfter(endOfDay))
                .collect(Collectors.toList());
    }
}