package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.EmailNotification;
import java.util.List;

public interface EmailNotificationPort {
    void sendEmail(EmailNotification emailNotification);
    void sendBulkEmails(List<EmailNotification> emailNotifications);
}