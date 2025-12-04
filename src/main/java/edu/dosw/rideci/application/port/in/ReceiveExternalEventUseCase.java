package edu.dosw.rideci.application.port.in;

public interface ReceiveExternalEventUseCase {
    void receive(NotificationEvent event);
}
