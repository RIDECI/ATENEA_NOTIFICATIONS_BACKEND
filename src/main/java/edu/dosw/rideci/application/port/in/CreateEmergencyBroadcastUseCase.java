package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.AppNotification;
import edu.dosw.rideci.domain.model.Enum.Category;
import java.util.List;

public interface CreateEmergencyBroadcastUseCase {
    List<AppNotification> createEmergencyBroadcast(CreateEmergencyBroadcastCommand command);

    record CreateEmergencyBroadcastCommand(
            String emergencyMessage,
            Category category,
            List<String> targetUserIds,
            String priorityLevel
    ) {}
}