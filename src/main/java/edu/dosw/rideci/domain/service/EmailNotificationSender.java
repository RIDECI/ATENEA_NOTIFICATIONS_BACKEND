package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.InAppNotification;

public interface EmailNotificationSender {
    void sendNotification(InAppNotification notification, String destinationEmail);
}
