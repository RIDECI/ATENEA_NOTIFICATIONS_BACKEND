package edu.dosw.rideci.unit.application.mapper;

import edu.dosw.rideci.application.mapper.NotificationApplicationMapper;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.NotificationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationApplicationMapperTest {

    private NotificationApplicationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NotificationApplicationMapper();
    }

    @Test
    void toInApp_ShouldReturnNullWhenEventIsNull() {
        InAppNotification result = mapper.toInApp(null);

        assertNull(result);
    }

    @Test
    void toInApp_ShouldMapEventToNotification() {
        String eventId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        NotificationEvent event = NotificationEvent.builder()
                .eventId(eventId)
                .eventType(NotificationType.TRIP_CREATED)
                .userId(userId)
                .message("Test message")
                .priority(1)
                .timestamp(Instant.now())
                .build();

        InAppNotification result = mapper.toInApp(event);

        assertNotNull(result);
        assertEquals(UUID.fromString(eventId), result.getNotificationId());
        assertEquals(UUID.fromString(userId), result.getUserId());
        assertEquals("Nuevo viaje creado", result.getTitle());
        assertEquals("Test message", result.getMessage());
        assertEquals(NotificationType.TRIP_CREATED, result.getEventType());
        assertEquals("1", result.getPriority());
        assertEquals(NotificationStatus.UNREAD, result.getStatus());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void toInApp_ShouldHandleNullEventId() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId(null)
                .eventType(NotificationType.TRIP_CREATED)
                .message("Test")
                .timestamp(Instant.now())
                .build();

        InAppNotification result = mapper.toInApp(event);

        assertNotNull(result);
        assertNull(result.getNotificationId());
    }

    @Test
    void toInApp_ShouldHandleNullUserId() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(NotificationType.TRIP_CREATED)
                .userId(null)
                .message("Test")
                .timestamp(Instant.now())
                .build();

        InAppNotification result = mapper.toInApp(event);

        assertNotNull(result);
        assertNull(result.getUserId());
    }

    @Test
    void toInApp_ShouldUseCurrentTimeWhenTimestampIsNull() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(NotificationType.TRIP_CREATED)
                .timestamp(null)
                .build();

        InAppNotification result = mapper.toInApp(event);

        assertNotNull(result);
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void fromEvent_ShouldReturnNullWhenEventIsNull() {
        InAppNotification result = mapper.fromEvent(null, "Title", "Message");

        assertNull(result);
    }

    @Test
    void fromEvent_ShouldOverrideTitleAndMessage() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(NotificationType.TRIP_CREATED)
                .message("Original message")
                .timestamp(Instant.now())
                .build();

        InAppNotification result = mapper.fromEvent(event, "Custom Title", "Custom Message");

        assertNotNull(result);
        assertEquals("Custom Title", result.getTitle());
        assertEquals("Custom Message", result.getMessage());
    }

    @Test
    void fromEvent_ShouldIgnoreBlankOverrides() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(NotificationType.TRIP_CREATED)
                .message("Original message")
                .timestamp(Instant.now())
                .build();

        InAppNotification result = mapper.fromEvent(event, "   ", "");

        assertNotNull(result);
        assertEquals("Nuevo viaje creado", result.getTitle());
        assertEquals("Original message", result.getMessage());
    }

    @Test
    void toInApp_ShouldMapDifferentNotificationTypes() {
        NotificationType[] types = {
                NotificationType.TRIP_CANCELLED,
                NotificationType.TRIP_COMPLETED,
                NotificationType.PAYMENT_CONFIRMED,
                NotificationType.PAYMENT_FAILED,
                NotificationType.EMERGENCY_BUTTON_PRESSED,
                NotificationType.LOCATION_ALERT,
                NotificationType.DRIVER_VALIDATED,
                NotificationType.USER_REGISTERED
        };

        for (NotificationType type : types) {
            NotificationEvent event = NotificationEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType(type)
                    .timestamp(Instant.now())
                    .build();

            InAppNotification result = mapper.toInApp(event);

            assertNotNull(result);
            assertEquals(type, result.getEventType());
            assertNotNull(result.getTitle());
        }
    }

    @Test
    void toInApp_ShouldHandleNullNotificationType() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(null)
                .timestamp(Instant.now())
                .build();

        InAppNotification result = mapper.toInApp(event);

        assertNotNull(result);
        assertEquals("Notification", result.getTitle());
    }
}

