package edu.dosw.rideci.unit.application.service;

import edu.dosw.rideci.application.mapper.NotificationApplicationMapper;
import edu.dosw.rideci.application.port.in.CreateNotificationUseCase;
import edu.dosw.rideci.application.service.InAppNotificationHandler;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.NotificationEvent;
import edu.dosw.rideci.domain.service.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InAppNotificationHandlerTest {

    @Mock
    private EventBus eventBus;

    @Mock
    private CreateNotificationUseCase createNotificationUseCase;

    @Mock
    private NotificationApplicationMapper notificationApplicationMapper;

    private InAppNotificationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new InAppNotificationHandler(
                eventBus,
                createNotificationUseCase,
                notificationApplicationMapper
        );
    }

    @Test
    void register_ShouldSubscribeToAllEventTypes() {
        handler.register();

        verify(eventBus, times(8)).subscribe(any(NotificationType.class), eq(handler));
    }

    @Test
    void handleEvent_ShouldCreateNotificationForTripCreated() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("event-1")
                .eventType(NotificationType.TRIP_CREATED)
                .userId("user-123")
                .message("Trip created")
                .timestamp(Instant.now())
                .build();

        InAppNotification notification = new InAppNotification();
        when(notificationApplicationMapper.fromEvent(eq(event), eq("New trip created"), anyString()))
                .thenReturn(notification);
        when(createNotificationUseCase.createNotification(notification)).thenReturn(notification);

        handler.handleEvent(event);

        verify(notificationApplicationMapper).fromEvent(eq(event), eq("New trip created"), anyString());
        verify(createNotificationUseCase).createNotification(notification);
    }

    @Test
    void handleEvent_ShouldCreateNotificationForTripCancelled() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("event-1")
                .eventType(NotificationType.TRIP_CANCELLED)
                .userId("user-123")
                .message("Trip cancelled")
                .timestamp(Instant.now())
                .build();

        InAppNotification notification = new InAppNotification();
        when(notificationApplicationMapper.fromEvent(eq(event), eq("Trip cancelled"), anyString()))
                .thenReturn(notification);
        when(createNotificationUseCase.createNotification(notification)).thenReturn(notification);

        handler.handleEvent(event);

        verify(notificationApplicationMapper).fromEvent(eq(event), eq("Trip cancelled"), anyString());
        verify(createNotificationUseCase).createNotification(notification);
    }

    @Test
    void handleEvent_ShouldCreateNotificationForTripCompleted() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("event-1")
                .eventType(NotificationType.TRIP_COMPLETED)
                .userId("user-123")
                .message("Trip completed")
                .timestamp(Instant.now())
                .build();

        InAppNotification notification = new InAppNotification();
        when(notificationApplicationMapper.fromEvent(eq(event), eq("Trip completed"), anyString()))
                .thenReturn(notification);
        when(createNotificationUseCase.createNotification(notification)).thenReturn(notification);

        handler.handleEvent(event);

        verify(notificationApplicationMapper).fromEvent(eq(event), eq("Trip completed"), anyString());
        verify(createNotificationUseCase).createNotification(notification);
    }

    @Test
    void handleEvent_ShouldCreateNotificationForPaymentConfirmed() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("event-1")
                .eventType(NotificationType.PAYMENT_CONFIRMED)
                .userId("user-123")
                .message("Payment confirmed")
                .timestamp(Instant.now())
                .build();

        InAppNotification notification = new InAppNotification();
        when(notificationApplicationMapper.fromEvent(eq(event), eq("Payment confirmed"), anyString()))
                .thenReturn(notification);
        when(createNotificationUseCase.createNotification(notification)).thenReturn(notification);

        handler.handleEvent(event);

        verify(notificationApplicationMapper).fromEvent(eq(event), eq("Payment confirmed"), anyString());
        verify(createNotificationUseCase).createNotification(notification);
    }

    @Test
    void handleEvent_ShouldCreateNotificationForPaymentFailed() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("event-1")
                .eventType(NotificationType.PAYMENT_FAILED)
                .userId("user-123")
                .message("Payment failed")
                .timestamp(Instant.now())
                .build();

        InAppNotification notification = new InAppNotification();
        when(notificationApplicationMapper.fromEvent(eq(event), eq("Payment failed"), anyString()))
                .thenReturn(notification);
        when(createNotificationUseCase.createNotification(notification)).thenReturn(notification);

        handler.handleEvent(event);

        verify(notificationApplicationMapper).fromEvent(eq(event), eq("Payment failed"), anyString());
        verify(createNotificationUseCase).createNotification(notification);
    }

    @Test
    void handleEvent_ShouldCreateNotificationForEmergencyButtonPressed() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("event-1")
                .eventType(NotificationType.EMERGENCY_BUTTON_PRESSED)
                .userId("user-123")
                .message("Emergency button pressed")
                .timestamp(Instant.now())
                .build();

        InAppNotification notification = new InAppNotification();
        when(notificationApplicationMapper.fromEvent(eq(event), eq("Emergency alert"), anyString()))
                .thenReturn(notification);
        when(createNotificationUseCase.createNotification(notification)).thenReturn(notification);

        handler.handleEvent(event);

        verify(notificationApplicationMapper).fromEvent(eq(event), eq("Emergency alert"), anyString());
        verify(createNotificationUseCase).createNotification(notification);
    }

    @Test
    void handleEvent_ShouldCreateNotificationForLocationAlert() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("event-1")
                .eventType(NotificationType.LOCATION_ALERT)
                .userId("user-123")
                .message("Location alert")
                .timestamp(Instant.now())
                .build();

        InAppNotification notification = new InAppNotification();
        when(notificationApplicationMapper.fromEvent(eq(event), eq("Location alert"), anyString()))
                .thenReturn(notification);
        when(createNotificationUseCase.createNotification(notification)).thenReturn(notification);

        handler.handleEvent(event);

        verify(notificationApplicationMapper).fromEvent(eq(event), eq("Location alert"), anyString());
        verify(createNotificationUseCase).createNotification(notification);
    }

    @Test
    void handleEvent_ShouldCreateNotificationForSecurityIncident() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("event-1")
                .eventType(NotificationType.SECURITY_INCIDENT)
                .userId("user-123")
                .message("Security incident")
                .timestamp(Instant.now())
                .build();

        InAppNotification notification = new InAppNotification();
        when(notificationApplicationMapper.fromEvent(eq(event), eq("Security incident"), anyString()))
                .thenReturn(notification);
        when(createNotificationUseCase.createNotification(notification)).thenReturn(notification);

        handler.handleEvent(event);

        verify(notificationApplicationMapper).fromEvent(eq(event), eq("Security incident"), anyString());
        verify(createNotificationUseCase).createNotification(notification);
    }

    @Test
    void handleEvent_ShouldUseDefaultTitleForUnknownType() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("event-1")
                .eventType(NotificationType.NOTIFICATION_CREATED)
                .userId("user-123")
                .message("Unknown type")
                .timestamp(Instant.now())
                .build();

        InAppNotification notification = new InAppNotification();
        when(notificationApplicationMapper.fromEvent(eq(event), eq("Notification"), anyString()))
                .thenReturn(notification);
        when(createNotificationUseCase.createNotification(notification)).thenReturn(notification);

        handler.handleEvent(event);

        verify(notificationApplicationMapper).fromEvent(eq(event), eq("Notification"), anyString());
    }

    @Test
    void getSubscribedEvents_ShouldReturnCorrectEventTypes() {
        List<NotificationType> subscribedEvents = handler.getSubscribedEvents();

        assertNotNull(subscribedEvents);
        assertEquals(8, subscribedEvents.size());
        assertTrue(subscribedEvents.contains(NotificationType.TRIP_CREATED));
        assertTrue(subscribedEvents.contains(NotificationType.TRIP_CANCELLED));
        assertTrue(subscribedEvents.contains(NotificationType.TRIP_COMPLETED));
        assertTrue(subscribedEvents.contains(NotificationType.PAYMENT_CONFIRMED));
        assertTrue(subscribedEvents.contains(NotificationType.PAYMENT_FAILED));
        assertTrue(subscribedEvents.contains(NotificationType.EMERGENCY_BUTTON_PRESSED));
        assertTrue(subscribedEvents.contains(NotificationType.LOCATION_ALERT));
        assertTrue(subscribedEvents.contains(NotificationType.SECURITY_INCIDENT));
    }

    @Test
    void getName_ShouldReturnCorrectHandlerId() {
        assertEquals("in-app-notification-handler", handler.getName());
    }

    @Test
    void handleEvent_ShouldUseEventToJSONAsMessage() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("event-1")
                .eventType(NotificationType.TRIP_CREATED)
                .userId("user-123")
                .message("Test message")
                .timestamp(Instant.now())
                .build();

        String expectedJson = event.toJSON();
        InAppNotification notification = new InAppNotification();
        when(notificationApplicationMapper.fromEvent(eq(event), anyString(), eq(expectedJson)))
                .thenReturn(notification);
        when(createNotificationUseCase.createNotification(notification)).thenReturn(notification);

        handler.handleEvent(event);

        verify(notificationApplicationMapper).fromEvent(eq(event), anyString(), eq(expectedJson));
    }
}
