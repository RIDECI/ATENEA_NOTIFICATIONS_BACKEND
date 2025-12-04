package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.AppNotification;
import java.util.List;

public interface NotificationBroadcastPort {
    void broadcastToAllUsers(AppNotification notification);
    void broadcastToSpecificUsers(AppNotification notification, List<String> userIds);
    void sendEmergencyAlert(AppNotification notification, String priorityLevel);
}