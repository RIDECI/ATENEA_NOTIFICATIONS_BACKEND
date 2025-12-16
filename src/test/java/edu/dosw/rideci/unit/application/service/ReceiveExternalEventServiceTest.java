package edu.dosw.rideci.unit.application.service;

import edu.dosw.rideci.application.service.ReceiveExternalEventService;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.NotificationEvent;
import edu.dosw.rideci.domain.service.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiveExternalEventServiceTest {

    @Mock
    private EventBus eventBus;

    private ReceiveExternalEventService service;

    @BeforeEach
    void setUp() {
        service = new ReceiveExternalEventService(eventBus);
    }

    @Test
    void receive_ShouldPublishEventToEventBus() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("test-event-id")
                .eventType(NotificationType.NOTIFICATION_CREATED)
                .sourceModule("TEST_MODULE")
                .userId("user-123")
                .message("Test message")
                .priority(1)
                .timestamp(Instant.now())
                .build();

        service.receive(event);

        verify(eventBus, times(1)).publish(event);
    }

    @Test
    void receive_ShouldHandleNullEvent() {
        service.receive(null);

        verify(eventBus, times(1)).publish(null);
    }

    @Test
    void receive_ShouldPublishMultipleEvents() {
        NotificationEvent event1 = NotificationEvent.builder()
                .eventId("event-1")
                .eventType(NotificationType.TRIP_CREATED)
                .timestamp(Instant.now())
                .build();

        NotificationEvent event2 = NotificationEvent.builder()
                .eventId("event-2")
                .eventType(NotificationType.PAYMENT_CONFIRMED)
                .timestamp(Instant.now())
                .build();

        service.receive(event1);
        service.receive(event2);

        verify(eventBus, times(1)).publish(event1);
        verify(eventBus, times(1)).publish(event2);
    }
}

