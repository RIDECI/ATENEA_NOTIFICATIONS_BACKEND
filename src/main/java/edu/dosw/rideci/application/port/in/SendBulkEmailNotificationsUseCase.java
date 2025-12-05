package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.EmailNotification;
import edu.dosw.rideci.domain.model.Enum.EmailType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SendBulkEmailNotificationsUseCase {

    List<EmailNotification> sendBulk(List<UUID> userIds,
                                     EmailType emailType,
                                     Map<String, String> templateData);
}
