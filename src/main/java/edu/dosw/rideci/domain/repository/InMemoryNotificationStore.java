package edu.dosw.rideci.domain.repository;

import edu.dosw.rideci.domain.model.InAppNotification;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryNotificationStore {

    private final Map<String, InAppNotification> notifications = new ConcurrentHashMap<>();

    public InAppNotification save(InAppNotification notification) {
        if (notification.getNotificationId() == null) {
            notification.setNotificationId(UUID.randomUUID());
        }
        String key = notification.getNotificationId().toString();
        notifications.put(key, notification);
        return notification;
    }

    public Optional<InAppNotification> findById(String id) {
        return Optional.ofNullable(notifications.get(id));
    }

    public List<InAppNotification> findAll() {
        return new ArrayList<>(notifications.values());
    }

    public void delete(String id) {
        notifications.remove(id);
    }

    public long count() {
        return notifications.size();
    }
}
