package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.InAppNotification;

public interface CreateNotificationUseCase {

    InAppNotification createNotification(InAppNotification notification);
}
