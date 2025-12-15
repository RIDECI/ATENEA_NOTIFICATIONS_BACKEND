package edu.dosw.rideci.unit.domain.service;

import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.service.NotificationDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationDomainServiceTest {

    private NotificationDomainService notificationDomainService;
    private InAppNotification notification;

    @BeforeEach
    void setUp() {
        notificationDomainService = new NotificationDomainService();
        notification = mock(InAppNotification.class);
    }

    @Test
    void initializeNotification_ShouldSetUnreadStatusAndCreatedAt() {
        when(notification.getCreatedAt()).thenReturn(null);

        InAppNotification result = notificationDomainService.initializeNotification(notification);

        verify(notification).setStatus(NotificationStatus.UNREAD);
        verify(notification).setCreatedAt(any(OffsetDateTime.class));
        assertEquals(notification, result);
    }

    @Test
    void initializeNotification_ShouldNotOverrideExistingCreatedAt() {
        OffsetDateTime existingDate = OffsetDateTime.now(ZoneOffset.UTC).minusDays(1);
        when(notification.getCreatedAt()).thenReturn(existingDate);

        notificationDomainService.initializeNotification(notification);

        verify(notification).setStatus(NotificationStatus.UNREAD);
        verify(notification, never()).setCreatedAt(any());
    }

    @Test
    void initializeNotification_ShouldThrowExceptionWhenNotificationIsNull() {
        assertThrows(NullPointerException.class, () -> {
            notificationDomainService.initializeNotification(null);
        });
    }

    @Test
    void markAsRead_ShouldSetReadStatusAndReadAt() {
        InAppNotification result = notificationDomainService.markAsRead(notification);

        verify(notification).setStatus(NotificationStatus.READ);
        verify(notification).setReadAt(any(OffsetDateTime.class));
        assertEquals(notification, result);
    }

    @Test
    void markAsRead_ShouldThrowExceptionWhenNotificationIsNull() {
        assertThrows(NullPointerException.class, () -> {
            notificationDomainService.markAsRead(null);
        });
    }

    @Test
    void archive_ShouldSetArchivedStatus() {
        InAppNotification result = notificationDomainService.archive(notification);

        verify(notification).setStatus(NotificationStatus.ARCHIVED);
        verify(notification, never()).setReadAt(any());
        verify(notification, never()).setExpiresAt(any());
        assertEquals(notification, result);
    }

    @Test
    void archive_ShouldThrowExceptionWhenNotificationIsNull() {
        assertThrows(NullPointerException.class, () -> {
            notificationDomainService.archive(null);
        });
    }

    @Test
    void expire_ShouldSetExpiredStatusAndExpiresAt() {
        InAppNotification result = notificationDomainService.expire(notification);

        verify(notification).setStatus(NotificationStatus.EXPIRED);
        verify(notification).setExpiresAt(any(OffsetDateTime.class));
        assertEquals(notification, result);
    }

    @Test
    void expire_ShouldThrowExceptionWhenNotificationIsNull() {
        assertThrows(NullPointerException.class, () -> {
            notificationDomainService.expire(null);
        });
    }

    @Test
    void isExpired_ShouldReturnTrueWhenNotificationIsExpired() {
        InAppNotification expiredNotification = mock(InAppNotification.class);
        when(expiredNotification.isExpired()).thenReturn(true);

        boolean result = notificationDomainService.isExpired(expiredNotification);

        assertTrue(result);
    }

    @Test
    void isExpired_ShouldReturnFalseWhenNotificationIsNotExpired() {
        InAppNotification validNotification = mock(InAppNotification.class);
        when(validNotification.isExpired()).thenReturn(false);

        boolean result = notificationDomainService.isExpired(validNotification);

        assertFalse(result);
    }

    @Test
    void isExpired_ShouldReturnFalseWhenNotificationIsNull() {
        boolean result = notificationDomainService.isExpired(null);

        assertFalse(result);
    }

    @Test
    void allMethods_ShouldReturnSameNotificationInstance() {
        InAppNotification testNotification = new InAppNotification();

        InAppNotification result1 = notificationDomainService.initializeNotification(testNotification);
        assertSame(testNotification, result1);

        testNotification.setStatus(NotificationStatus.READ);
        InAppNotification result2 = notificationDomainService.archive(testNotification);
        assertSame(testNotification, result2);

        InAppNotification result3 = notificationDomainService.expire(testNotification);
        assertSame(testNotification, result3);
    }

    @Test
    void testMethodChaining() {
        InAppNotification notification = new InAppNotification();

        notificationDomainService.initializeNotification(notification);
        assertEquals(NotificationStatus.UNREAD, notification.getStatus());
        assertNotNull(notification.getCreatedAt());

        notificationDomainService.markAsRead(notification);
        assertEquals(NotificationStatus.READ, notification.getStatus());
        assertNotNull(notification.getReadAt());

        notificationDomainService.archive(notification);
        assertEquals(NotificationStatus.ARCHIVED, notification.getStatus());
    }
}
