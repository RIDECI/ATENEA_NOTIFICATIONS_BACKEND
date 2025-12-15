package edu.dosw.rideci.unit.domain.service;

import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.NotificationEvent;
import edu.dosw.rideci.domain.service.EventBus;
import edu.dosw.rideci.domain.service.TripCreatedNotificationSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripCreatedNotificationSubscriberTest {

    @Mock
    private EventBus eventBus;

    @Mock
    private NotificationEvent notificationEvent;

    private TripCreatedNotificationSubscriber subscriber;

    @BeforeEach
    void setUp() {
        subscriber = new TripCreatedNotificationSubscriber(eventBus);
    }

    @Test
    void init_ShouldSubscribeToEventBus() {
        subscriber.init();

        verify(eventBus).subscribe(eq(NotificationType.TRIP_CREATED), eq(subscriber));
    }

    @Test
    void destroy_ShouldUnsubscribeFromEventBus() {
        subscriber.init();

        subscriber.destroy();

        verify(eventBus).unsubscribe(eq(NotificationType.TRIP_CREATED), eq(subscriber));
    }

    @Test
    void handleEvent_ShouldProcessEvent() {
        when(notificationEvent.getUserId()).thenReturn("user123");

        subscriber.handleEvent(notificationEvent);

        verify(notificationEvent).getUserId();
    }

    @Test
    void handleEvent_ShouldIgnoreNullEvent() {
        subscriber.handleEvent(null);

        assertDoesNotThrow(() -> subscriber.handleEvent(null));
    }

    @Test
    void getSubscribedEvents_ShouldReturnTripCreatedOnly() {
        List<NotificationType> events = subscriber.getSubscribedEvents();

        assertEquals(List.of(NotificationType.TRIP_CREATED), events);
    }

    @Test
    void getName_ShouldReturnCorrectName() {
        assertEquals("trip-created-notification-subscriber", subscriber.getName());
    }

    @Test
    void constructor_ShouldInitializeWithEventBus() {
        assertNotNull(subscriber);
        subscriber.init();
        verify(eventBus).subscribe(eq(NotificationType.TRIP_CREATED), eq(subscriber));
    }
}