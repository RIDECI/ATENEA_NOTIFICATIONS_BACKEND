package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.NotificationEvent;

public interface ReceiveExternalEventUseCase {
    void receive(NotificationEvent event);
}
