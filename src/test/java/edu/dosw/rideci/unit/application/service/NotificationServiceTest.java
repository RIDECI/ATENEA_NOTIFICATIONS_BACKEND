package edu.dosw.rideci.unit.application.service;

import edu.dosw.rideci.application.service.NotificationService;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.NotificationEvent;
import edu.dosw.rideci.domain.service.EventBus;
import edu.dosw.rideci.domain.service.NotificationDomainService;
import edu.dosw.rideci.exceptions.NotificationNotFoundException;
import edu.dosw.rideci.application.port.out.NotificationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepositoryPort notificationRepositoryPort;

    @Mock
    private NotificationDomainService domainService;

    @Mock
    private EventBus eventBus;

    private NotificationService service;

    @BeforeEach
    void setUp() {
        service = new NotificationService(notificationRepositoryPort, domainService, eventBus);
    }

    @Test
    void createNotification_ShouldInitializeSaveAndPublishEvent() {
        InAppNotification notification = createTestNotification();
        when(notificationRepositoryPort.save(any(InAppNotification.class))).thenReturn(notification);

        InAppNotification result = service.createNotification(notification);

        verify(domainService).initializeNotification(notification);
        verify(notificationRepositoryPort).save(notification);
        verify(eventBus).publish(any(NotificationEvent.class));
        assertNotNull(result);
    }

    @Test
    void createNotification_ShouldPublishEventWithCorrectData() {
        InAppNotification notification = createTestNotification();
        UUID userId = notification.getUserId();
        when(notificationRepositoryPort.save(any(InAppNotification.class))).thenReturn(notification);

        service.createNotification(notification);

        ArgumentCaptor<NotificationEvent> eventCaptor = ArgumentCaptor.forClass(NotificationEvent.class);
        verify(eventBus).publish(eventCaptor.capture());

        NotificationEvent publishedEvent = eventCaptor.getValue();
        assertEquals(NotificationType.NOTIFICATION_CREATED, publishedEvent.getEventType());
        assertEquals("ATENEA_NOTIFICATIONS_BACKEND", publishedEvent.getSourceModule());
        assertEquals(userId.toString(), publishedEvent.getUserId());
    }

    @Test
    void getNotificationsByUserId_ShouldReturnOrderedList() {
        UUID userId = UUID.randomUUID();
        InAppNotification notification1 = createTestNotification();
        notification1.setUserId(userId);
        InAppNotification notification2 = createTestNotification();
        notification2.setUserId(userId);

        List<InAppNotification> expectedList = Arrays.asList(notification1, notification2);
        when(notificationRepositoryPort.findByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(expectedList);

        List<InAppNotification> result = service.getNotificationsByUserId(userId);

        assertEquals(2, result.size());
        verify(notificationRepositoryPort).findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Test
    void markAsRead_ShouldMarkNotificationAsRead() {
        UUID notificationId = UUID.randomUUID();
        InAppNotification notification = createTestNotification();
        notification.setNotificationId(notificationId);

        when(notificationRepositoryPort.findById(notificationId))
                .thenReturn(Optional.of(notification));
        when(notificationRepositoryPort.save(any(InAppNotification.class)))
                .thenReturn(notification);

        InAppNotification result = service.markAsRead(notificationId);

        verify(domainService).markAsRead(notification);
        verify(notificationRepositoryPort).save(notification);
        assertNotNull(result);
    }

    @Test
    void markAsRead_ShouldThrowExceptionWhenNotFound() {
        UUID notificationId = UUID.randomUUID();
        when(notificationRepositoryPort.findById(notificationId))
                .thenReturn(Optional.empty());

        assertThrows(NotificationNotFoundException.class, () -> {
            service.markAsRead(notificationId);
        });

        verify(domainService, never()).markAsRead(any());
    }

    @Test
    void createNotification_ShouldHandleNullUserId() {
        InAppNotification notification = createTestNotification();
        notification.setUserId(null);
        when(notificationRepositoryPort.save(any(InAppNotification.class))).thenReturn(notification);

        InAppNotification result = service.createNotification(notification);

        ArgumentCaptor<NotificationEvent> eventCaptor = ArgumentCaptor.forClass(NotificationEvent.class);
        verify(eventBus).publish(eventCaptor.capture());

        NotificationEvent publishedEvent = eventCaptor.getValue();
        assertNull(publishedEvent.getUserId());
        assertNotNull(result);
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

