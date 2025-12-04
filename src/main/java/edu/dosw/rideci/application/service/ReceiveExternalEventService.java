package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.ReceiveExternalEventUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiveExternalEventService implements ReceiveExternalEventUseCase {

    private final EventBus eventBus;

    @Override
    public void receive(NotificationEvent event) {
        eventBus.publish(event);
    }
}
