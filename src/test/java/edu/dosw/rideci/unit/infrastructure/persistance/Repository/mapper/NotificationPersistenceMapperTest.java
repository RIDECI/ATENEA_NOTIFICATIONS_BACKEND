package edu.dosw.rideci.unit.infrastructure.persistance.Repository.mapper;

import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.infrastructure.persistance.Entity.NotificationEntity;
import edu.dosw.rideci.infrastructure.persistance.Repository.mapper.NotificationPersistenceMapper;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationPersistenceMapperTest {

    @Test
    void toEntity_ShouldReturnNullWhenNotificationIsNull() {
        NotificationEntity result = NotificationPersistenceMapper.toEntity(null);

        assertNull(result);
    }

    @Test
    void toEntity_ShouldMapNotificationToEntity() {
        UUID notificationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        InAppNotification notification = InAppNotification.builder()
                .notificationId(notificationId)
                .userId(userId)
                .title("Test Title")
                .message("Test Message")
                .eventType(NotificationType.TRIP_CREATED)
                .priority("HIGH")
                .status(NotificationStatus.UNREAD)
                .createdAt(OffsetDateTime.now())
                .readAt(null)
                .expiresAt(null)
                .build();

        NotificationEntity entity = NotificationPersistenceMapper.toEntity(notification);

        assertNotNull(entity);
        assertEquals(notificationId, entity.getNotificationId());
        assertEquals(userId, entity.getUserId());
        assertEquals("Test Title", entity.getTitle());
        assertEquals("Test Message", entity.getMessage());
        assertEquals(NotificationType.TRIP_CREATED, entity.getEventType());
        assertEquals("HIGH", entity.getPriority());
        assertEquals(NotificationStatus.UNREAD, entity.getStatus());
        assertNotNull(entity.getCreatedAt());
    }

    @Test
    void toEntity_ShouldHandleNullPriority() {
        InAppNotification notification = InAppNotification.builder()
                .notificationId(UUID.randomUUID())
                .priority(null)
                .build();

        NotificationEntity entity = NotificationPersistenceMapper.toEntity(notification);

        assertNull(entity.getPriority());
    }

    @Test
    void toDomain_ShouldReturnNullWhenEntityIsNull() {
        InAppNotification result = NotificationPersistenceMapper.toDomain(null);

        assertNull(result);
    }

    @Test
    void toDomain_ShouldMapEntityToNotification() {
        UUID notificationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        NotificationEntity entity = NotificationEntity.builder()
                .notificationId(notificationId)
                .userId(userId)
                .title("Test Title")
                .message("Test Message")
                .eventType(NotificationType.TRIP_CREATED)
                .priority("HIGH")
                .status(NotificationStatus.UNREAD)
                .createdAt(OffsetDateTime.now())
                .readAt(null)
                .expiresAt(null)
                .build();

        InAppNotification notification = NotificationPersistenceMapper.toDomain(entity);

        assertNotNull(notification);
        assertEquals(notificationId, notification.getNotificationId());
        assertEquals(userId, notification.getUserId());
        assertEquals("Test Title", notification.getTitle());
        assertEquals("Test Message", notification.getMessage());
        assertEquals(NotificationType.TRIP_CREATED, notification.getEventType());
        assertEquals("HIGH", notification.getPriority());
        assertEquals(NotificationStatus.UNREAD, notification.getStatus());
        assertNotNull(notification.getCreatedAt());
    }

    @Test
    void toEntityAndToDomain_ShouldBeReversible() {
        UUID notificationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();

        InAppNotification original = InAppNotification.builder()
                .notificationId(notificationId)
                .userId(userId)
                .title("Original Title")
                .message("Original Message")
                .eventType(NotificationType.PAYMENT_CONFIRMED)
                .priority("NORMAL")
                .status(NotificationStatus.READ)
                .createdAt(createdAt)
                .readAt(createdAt.plusHours(1))
                .expiresAt(createdAt.plusDays(30))
                .build();

        NotificationEntity entity = NotificationPersistenceMapper.toEntity(original);
        InAppNotification restored = NotificationPersistenceMapper.toDomain(entity);

        assertEquals(original.getNotificationId(), restored.getNotificationId());
        assertEquals(original.getUserId(), restored.getUserId());
        assertEquals(original.getTitle(), restored.getTitle());
        assertEquals(original.getMessage(), restored.getMessage());
        assertEquals(original.getEventType(), restored.getEventType());
        assertEquals(original.getPriority(), restored.getPriority());
        assertEquals(original.getStatus(), restored.getStatus());
        assertEquals(original.getCreatedAt(), restored.getCreatedAt());
        assertEquals(original.getReadAt(), restored.getReadAt());
        assertEquals(original.getExpiresAt(), restored.getExpiresAt());
    }
}

