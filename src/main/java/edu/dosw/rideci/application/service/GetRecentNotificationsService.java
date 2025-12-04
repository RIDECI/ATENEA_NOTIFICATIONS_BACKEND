package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.GetRecentNotificationsUseCase;
import edu.dosw.rideci.application.port.out.AppNotificationRepositoryPort;
import edu.dosw.rideci.domain.model.AppNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GetRecentNotificationsService implements GetRecentNotificationsUseCase {

    private final AppNotificationRepositoryPort appNotificationRepositoryPort;

    @Autowired
    public GetRecentNotificationsService(AppNotificationRepositoryPort appNotificationRepositoryPort) {
        this.appNotificationRepositoryPort = appNotificationRepositoryPort;
    }

    @Override
    public List<AppNotification> getRecentNotifications(GetRecentNotificationsCommand command) {
        if (command.limit() <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }

        if (command.userId() != null && !command.userId().isEmpty()) {
            return appNotificationRepositoryPort
                    .findTopNByUserIdOrderByTimestampDesc(command.userId(), command.limit());
        } else {
            return appNotificationRepositoryPort
                    .findTopNByOrderByTimestampDesc(command.limit());
        }
    }
}
