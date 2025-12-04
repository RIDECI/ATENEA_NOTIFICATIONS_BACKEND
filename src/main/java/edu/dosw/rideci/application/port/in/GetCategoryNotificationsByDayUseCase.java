package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.AppNotification;
import edu.dosw.rideci.domain.model.Enum.Category;
import java.time.LocalDate;
import java.util.List;

public interface GetCategoryNotificationsByDayUseCase {
    List<AppNotification> getCategoryNotificationsByDay(GetCategoryNotificationsByDayCommand command);

    record GetCategoryNotificationsByDayCommand(
            Category category,
            LocalDate date,
            String userId
    ) {}
}