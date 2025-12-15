package edu.dosw.rideci.unit.infrastructure.controller.dto;

import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.infrastructure.controller.dto.NotificationDtoMapper;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreateNotificationRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.NotificationResponse;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDtoMapperTest {

    @Test
    void toDomain_ShouldMapRequestToNotification() {
        UUID userId = UUID.randomUUID();
        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setUserId(userId);
        request.setTitle("Test Title");
        request.setMessage("Test Message");
        request.setEventType(NotificationType.TRIP_CREATED);
        request.setPriority("HIGH");

        InAppNotification notification = NotificationDtoMapper.toDomain(request);

        assertNotNull(notification);
        assertNull(notification.getNotificationId());
        assertEquals(userId, notification.getUserId());
        assertEquals("Test Title", notification.getTitle());
        assertEquals("Test Message", notification.getMessage());
        assertEquals(NotificationType.TRIP_CREATED, notification.getEventType());
        assertEquals("HIGH", notification.getPriority());
        assertNotNull(notification.getCreatedAt());
    }

    @Test
    void toResponse_ShouldMapNotificationToResponse() {
        UUID notificationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime readAt = createdAt.plusHours(1);
        OffsetDateTime expiresAt = createdAt.plusDays(30);

        InAppNotification notification = InAppNotification.builder()
                .notificationId(notificationId)
                .userId(userId)
                .title("Test Title")
                .message("Test Message")
                .eventType(NotificationType.PAYMENT_CONFIRMED)
                .priority("NORMAL")
                .status(NotificationStatus.READ)
                .createdAt(createdAt)
                .readAt(readAt)
                .expiresAt(expiresAt)
                .build();

        NotificationResponse response = NotificationDtoMapper.toResponse(notification);

        assertNotNull(response);
        assertEquals(notificationId, response.getNotificationId());
        assertEquals(userId, response.getUserId());
        assertEquals(NotificationType.PAYMENT_CONFIRMED, response.getEventType());
        assertNull(response.getChannel());
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Message", response.getMessage());
        assertEquals("NORMAL", response.getPriority());
        assertNull(response.getMetadataJson());
        assertEquals(NotificationStatus.READ, response.getStatus());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(readAt, response.getReadAt());
        assertEquals(expiresAt, response.getExpiresAt());
    }

    @Test
    void toResponse_ShouldHandleNullFields() {
        InAppNotification notification = InAppNotification.builder()
                .notificationId(UUID.randomUUID())
                .userId(null)
                .title(null)
                .message(null)
                .eventType(null)
                .priority(null)
                .status(null)
                .createdAt(null)
                .readAt(null)
                .expiresAt(null)
                .build();

        NotificationResponse response = NotificationDtoMapper.toResponse(notification);

        assertNotNull(response);
        assertNotNull(response.getNotificationId());
        assertNull(response.getUserId());
        assertNull(response.getTitle());
        assertNull(response.getMessage());
        assertNull(response.getEventType());
        assertNull(response.getPriority());
        assertNull(response.getStatus());
    }
}

