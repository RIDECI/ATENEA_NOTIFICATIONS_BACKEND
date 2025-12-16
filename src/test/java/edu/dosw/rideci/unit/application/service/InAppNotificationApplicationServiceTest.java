package edu.dosw.rideci.unit.application.service;

import edu.dosw.rideci.application.service.InAppNotificationApplicationService;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.service.NotificationDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InAppNotificationApplicationServiceTest {

    @Mock
    private NotificationDomainService domainService;

    private InAppNotificationApplicationService service;

    @BeforeEach
    void setUp() {
        service = new InAppNotificationApplicationService(domainService);
    }

    @Test
    void createNotification_ShouldInitializeAndSave() {
        InAppNotification notification = new InAppNotification();
        notification.setTitle("Test Title");
        notification.setMessage("Test Message");

        InAppNotification result = service.createNotification(notification);

        verify(domainService).initializeNotification(notification);
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
    }

    @Test
    void list_ShouldReturnAllNotifications() {
        InAppNotification notification1 = createTestNotification();
        InAppNotification notification2 = createTestNotification();

        service.createNotification(notification1);
        service.createNotification(notification2);

        List<InAppNotification> result = service.list();

        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    @Test
    void get_ShouldReturnNotificationById() {
        InAppNotification notification = createTestNotification();
        service.createNotification(notification);
        String id = notification.getNotificationId().toString();

        InAppNotification result = service.get(id);

        assertNotNull(result);
        assertEquals(notification.getNotificationId(), result.getNotificationId());
    }

    @Test
    void get_ShouldThrowExceptionWhenNotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.get(UUID.randomUUID().toString());
        });
    }

    @Test
    void update_ShouldUpdateExistingNotification() {
        InAppNotification notification = createTestNotification();
        service.createNotification(notification);
        String id = notification.getNotificationId().toString();

        InAppNotification update = new InAppNotification();
        update.setTitle("Updated Title");
        update.setMessage("Updated Message");

        InAppNotification result = service.update(id, update);

        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Message", result.getMessage());
    }

    @Test
    void delete_ShouldRemoveNotification() {
        InAppNotification notification = createTestNotification();
        service.createNotification(notification);
        String id = notification.getNotificationId().toString();

        service.delete(id);

        assertThrows(IllegalArgumentException.class, () -> {
            service.get(id);
        });
    }

    @Test
    void markAsRead_ShouldMarkNotificationAsRead() {
        InAppNotification notification = createTestNotification();
        service.createNotification(notification);
        String id = notification.getNotificationId().toString();

        InAppNotification result = service.markAsRead(id);

        verify(domainService).markAsRead(any(InAppNotification.class));
        assertNotNull(result);
    }

    @Test
    void cancel_ShouldArchiveNotification() {
        InAppNotification notification = createTestNotification();
        service.createNotification(notification);
        String id = notification.getNotificationId().toString();

        InAppNotification result = service.cancel(id);

        verify(domainService).archive(any(InAppNotification.class));
        assertNotNull(result);
    }

    @Test
    void retry_ShouldReinitializeNotification() {
        InAppNotification notification = createTestNotification();
        service.createNotification(notification);
        String id = notification.getNotificationId().toString();

        InAppNotification result = service.retry(id);

        verify(domainService, times(2)).initializeNotification(any(InAppNotification.class));
        assertNotNull(result);
    }

    @Test
    void count_ShouldReturnCorrectCount() {
        long initialCount = service.count();

        InAppNotification notification1 = createTestNotification();
        InAppNotification notification2 = createTestNotification();

        service.createNotification(notification1);
        service.createNotification(notification2);

        long finalCount = service.count();
        assertEquals(initialCount + 2, finalCount);
    }

    @Test
    void getModuleName_ShouldReturnCorrectModuleName() {
        assertEquals("ATENEA_NOTIFICATIONS_BACKEND", service.getModuleName());
    }

    private InAppNotification createTestNotification() {
        InAppNotification notification = new InAppNotification();
        notification.setNotificationId(UUID.randomUUID());
        notification.setTitle("Test Title");
        notification.setMessage("Test Message");
        notification.setEventType(NotificationType.NOTIFICATION_CREATED);
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setCreatedAt(OffsetDateTime.now());
        return notification;
    }
}

