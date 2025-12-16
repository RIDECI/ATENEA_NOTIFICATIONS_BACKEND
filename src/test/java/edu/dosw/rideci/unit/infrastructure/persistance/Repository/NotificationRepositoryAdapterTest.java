package edu.dosw.rideci.unit.infrastructure.persistance.Repository;

import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.infrastructure.persistance.Repository.NotificationRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationRepositoryAdapterTest {

    private NotificationRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new NotificationRepositoryAdapter();
    }

    @Test
    void save_ShouldSaveNotification() {
        InAppNotification notification = createTestNotification();

        InAppNotification result = adapter.save(notification);

        assertNotNull(result);
        assertNotNull(result.getNotificationId());
        assertEquals(notification.getTitle(), result.getTitle());
    }

    @Test
    void save_ShouldGenerateIdWhenNull() {
        InAppNotification notification = createTestNotification();
        notification.setNotificationId(null);

        InAppNotification result = adapter.save(notification);

        assertNotNull(result.getNotificationId());
    }

    @Test
    void save_ShouldUpdateExistingNotification() {
        InAppNotification notification = createTestNotification();
        InAppNotification saved = adapter.save(notification);

        saved.setTitle("Updated Title");
        InAppNotification updated = adapter.save(saved);

        assertEquals("Updated Title", updated.getTitle());
        assertEquals(saved.getNotificationId(), updated.getNotificationId());
    }

    @Test
    void findById_ShouldReturnNotificationWhenExists() {
        InAppNotification notification = createTestNotification();
        InAppNotification saved = adapter.save(notification);
        UUID id = saved.getNotificationId();

        Optional<InAppNotification> result = adapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals(saved.getTitle(), result.get().getTitle());
    }

    @Test
    void findById_ShouldReturnEmptyWhenNotExists() {
        UUID nonExistentId = UUID.randomUUID();

        Optional<InAppNotification> result = adapter.findById(nonExistentId);

        assertFalse(result.isPresent());
    }

    @Test
    void findByUserIdOrderByCreatedAtDesc_ShouldReturnUserNotifications() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        InAppNotification notification1 = createTestNotification();
        notification1.setUserId(userId1);
        notification1.setCreatedAt(OffsetDateTime.now().minusHours(2));

        InAppNotification notification2 = createTestNotification();
        notification2.setUserId(userId1);
        notification2.setCreatedAt(OffsetDateTime.now().minusHours(1));

        InAppNotification notification3 = createTestNotification();
        notification3.setUserId(userId2);

        adapter.save(notification1);
        adapter.save(notification2);
        adapter.save(notification3);

        List<InAppNotification> result = adapter.findByUserIdOrderByCreatedAtDesc(userId1);

        assertEquals(2, result.size());
        assertEquals(notification2.getNotificationId(), result.get(0).getNotificationId());
        assertEquals(notification1.getNotificationId(), result.get(1).getNotificationId());
    }

    @Test
    void findByUserIdOrderByCreatedAtDesc_ShouldReturnEmptyListForNonExistentUser() {
        UUID nonExistentUserId = UUID.randomUUID();

        List<InAppNotification> result = adapter.findByUserIdOrderByCreatedAtDesc(nonExistentUserId);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserIdOrderByCreatedAtDesc_ShouldOrderByCreatedAtDescending() {
        UUID userId = UUID.randomUUID();

        InAppNotification oldNotification = createTestNotification();
        oldNotification.setUserId(userId);
        oldNotification.setCreatedAt(OffsetDateTime.now().minusDays(2));

        InAppNotification newNotification = createTestNotification();
        newNotification.setUserId(userId);
        newNotification.setCreatedAt(OffsetDateTime.now());

        adapter.save(oldNotification);
        adapter.save(newNotification);

        List<InAppNotification> result = adapter.findByUserIdOrderByCreatedAtDesc(userId);

        assertEquals(2, result.size());
        assertTrue(result.get(0).getCreatedAt().isAfter(result.get(1).getCreatedAt()));
    }

    private InAppNotification createTestNotification() {
        InAppNotification notification = new InAppNotification();
        notification.setNotificationId(UUID.randomUUID());
        notification.setUserId(UUID.randomUUID());
        notification.setTitle("Test Title");
        notification.setMessage("Test Message");
        notification.setEventType(NotificationType.NOTIFICATION_CREATED);
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setCreatedAt(OffsetDateTime.now());
        return notification;
    }
}

