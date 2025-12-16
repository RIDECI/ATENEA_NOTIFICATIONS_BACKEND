package edu.dosw.rideci.unit.infrastructure.controller;

import edu.dosw.rideci.application.service.InAppNotificationApplicationService;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.infrastructure.controller.NotificationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private InAppNotificationApplicationService notificationService;

    private NotificationController controller;

    @BeforeEach
    void setUp() {
        controller = new NotificationController(notificationService);
    }

    @Test
    void create_ShouldReturnCreatedNotification() {
        InAppNotification request = createTestNotification();
        InAppNotification created = createTestNotification();
        created.setNotificationId(UUID.randomUUID());

        when(notificationService.createNotification(any(InAppNotification.class))).thenReturn(created);

        ResponseEntity<InAppNotification> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(notificationService).createNotification(request);
    }

    @Test
    void list_ShouldReturnListOfNotifications() {
        List<InAppNotification> notifications = Arrays.asList(
                createTestNotification(),
                createTestNotification()
        );

        when(notificationService.list()).thenReturn(notifications);

        ResponseEntity<List<InAppNotification>> response = controller.list();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(notificationService).list();
    }

    @Test
    void get_ShouldReturnNotificationById() {
        InAppNotification notification = createTestNotification();
        String id = notification.getNotificationId().toString();

        when(notificationService.get(id)).thenReturn(notification);

        ResponseEntity<InAppNotification> response = controller.get(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(notification.getNotificationId(), response.getBody().getNotificationId());
        verify(notificationService).get(id);
    }

    @Test
    void update_ShouldReturnUpdatedNotification() {
        String id = UUID.randomUUID().toString();
        InAppNotification update = createTestNotification();
        update.setTitle("Updated Title");
        InAppNotification updated = createTestNotification();
        updated.setTitle("Updated Title");

        when(notificationService.update(id, update)).thenReturn(updated);

        ResponseEntity<InAppNotification> response = controller.update(id, update);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Title", response.getBody().getTitle());
        verify(notificationService).update(id, update);
    }

    @Test
    void delete_ShouldReturnNoContent() {
        String id = UUID.randomUUID().toString();
        doNothing().when(notificationService).delete(id);

        ResponseEntity<Void> response = controller.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(notificationService).delete(id);
    }

    @Test
    void markAsRead_ShouldReturnMarkedNotification() {
        String id = UUID.randomUUID().toString();
        InAppNotification notification = createTestNotification();
        notification.setStatus(NotificationStatus.READ);

        when(notificationService.markAsRead(id)).thenReturn(notification);

        ResponseEntity<InAppNotification> response = controller.markAsRead(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(NotificationStatus.READ, response.getBody().getStatus());
        verify(notificationService).markAsRead(id);
    }

    @Test
    void cancel_ShouldReturnCancelledNotification() {
        String id = UUID.randomUUID().toString();
        InAppNotification notification = createTestNotification();

        when(notificationService.cancel(id)).thenReturn(notification);

        ResponseEntity<InAppNotification> response = controller.cancel(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(notificationService).cancel(id);
    }

    @Test
    void retry_ShouldReturnRetriedNotification() {
        String id = UUID.randomUUID().toString();
        InAppNotification notification = createTestNotification();

        when(notificationService.retry(id)).thenReturn(notification);

        ResponseEntity<InAppNotification> response = controller.retry(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(notificationService).retry(id);
    }

    @Test
    void metrics_ShouldReturnMetrics() {
        long count = 10L;
        String moduleName = "ATENEA_NOTIFICATIONS_BACKEND";

        when(notificationService.count()).thenReturn(count);
        when(notificationService.getModuleName()).thenReturn(moduleName);

        ResponseEntity<?> response = controller.metrics();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> metrics = (Map<String, Object>) response.getBody();
        assertEquals(count, metrics.get("totalNotifications"));
        assertEquals(moduleName, metrics.get("module"));
        
        verify(notificationService).count();
        verify(notificationService).getModuleName();
    }

    @Test
    void send_ShouldReturnAccepted() {
        String id = UUID.randomUUID().toString();

        ResponseEntity<Void> response = controller.send(id);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNull(response.getBody());
    }

    private InAppNotification createTestNotification() {
        InAppNotification notification = new InAppNotification();
        notification.setNotificationId(UUID.randomUUID());
        notification.setUserId(UUID.randomUUID());
        notification.setTitle("Test Title");
        notification.setMessage("Test Message");
        notification.setEventType(NotificationType.NOTIFICATION_CREATED);
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setPriority("NORMAL");
        notification.setCreatedAt(OffsetDateTime.now());
        return notification;
    }
}

