package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.CreateEmergencyBroadcastUseCase;
import edu.dosw.rideci.application.port.out.AppNotificationRepositoryPort;
import edu.dosw.rideci.application.port.out.NotificationBroadcastPort;
import edu.dosw.rideci.application.port.out.UserRepositoryPort;
import edu.dosw.rideci.domain.model.AppNotification;
import edu.dosw.rideci.domain.model.Enum.Category;
import edu.dosw.rideci.domain.model.Enum.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreateEmergencyBroadcastService implements CreateEmergencyBroadcastUseCase {

    private final AppNotificationRepositoryPort appNotificationRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final NotificationBroadcastPort notificationBroadcastPort;

    @Autowired
    public CreateEmergencyBroadcastService(
            AppNotificationRepositoryPort appNotificationRepositoryPort,
            UserRepositoryPort userRepositoryPort,
            NotificationBroadcastPort notificationBroadcastPort) {
        this.appNotificationRepositoryPort = appNotificationRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.notificationBroadcastPort = notificationBroadcastPort;
    }

    @Override
    public List<AppNotification> createEmergencyBroadcast(CreateEmergencyBroadcastCommand command) {
        if (command.emergencyMessage() == null || command.emergencyMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency message is required");
        }

        if (command.category() != Category.ALERTS) {
            throw new IllegalArgumentException("Emergency broadcasts must be of category ALERTS");
        }

        List<String> targetUserIds = command.targetUserIds();
        List<AppNotification> createdNotifications = new ArrayList<>();

        if (targetUserIds == null || targetUserIds.isEmpty()) {
            List<AppNotification> allNotifications = createNotificationsForAllUsers(command);
            notificationBroadcastPort.broadcastToAllUsers(allNotifications.get(0));
            return allNotifications;
        } else {
            for (String userId : targetUserIds) {
                if (!userRepositoryPort.existsById(userId)) {
                    throw new IllegalArgumentException("User with ID " + userId + " does not exist");
                }

                AppNotification notification = createNotificationForUser(userId, command);
                createdNotifications.add(notification);
            }
            notificationBroadcastPort.broadcastToSpecificUsers(createdNotifications.get(0), targetUserIds);
        }

        return createdNotifications;
    }

    private List<AppNotification> createNotificationsForAllUsers(CreateEmergencyBroadcastCommand command) {
        List<AppNotification> notifications = new ArrayList<>();
        List<edu.dosw.rideci.domain.model.User> allUsers = userRepositoryPort.findAll();

        for (edu.dosw.rideci.domain.model.User user : allUsers) {
            AppNotification notification = createNotificationForUser(user.getId(), command);
            notifications.add(notification);
        }

        return notifications;
    }

    private AppNotification createNotificationForUser(String userId, CreateEmergencyBroadcastCommand command) {
        AppNotification notification = AppNotification.builder()
                .user(edu.dosw.rideci.domain.model.User.builder().id(userId).build())
                .messageType(MessageType.SECURITY_ALERT)
                .message(command.emergencyMessage())
                .timestamp(LocalDateTime.now())
                .category(command.category())
                .read(false)
                .build();

        return appNotificationRepositoryPort.save(notification);
    }
}