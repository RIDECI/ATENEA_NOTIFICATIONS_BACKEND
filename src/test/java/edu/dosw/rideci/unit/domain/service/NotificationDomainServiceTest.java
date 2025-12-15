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
        // Given
        when(notification.getCreatedAt()).thenReturn(null);

        // When
        InAppNotification result = notificationDomainService.initializeNotification(notification);

        // Then
        verify(notification).setStatus(NotificationStatus.UNREAD);
        verify(notification).setCreatedAt(any(OffsetDateTime.class));
        assertEquals(notification, result);
    }

    @Test
    void initializeNotification_ShouldNotOverrideExistingCreatedAt() {
        // Given
        OffsetDateTime existingDate = OffsetDateTime.now(ZoneOffset.UTC).minusDays(1);
        when(notification.getCreatedAt()).thenReturn(existingDate);

        // When
        notificationDomainService.initializeNotification(notification);

        // Then
        verify(notification).setStatus(NotificationStatus.UNREAD);
        verify(notification, never()).setCreatedAt(any());
    }

    @Test
    void initializeNotification_ShouldThrowExceptionWhenNotificationIsNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            notificationDomainService.initializeNotification(null);
        });
    }

    @Test
    void markAsRead_ShouldSetReadStatusAndReadAt() {
        // When
        InAppNotification result = notificationDomainService.markAsRead(notification);

        // Then
        verify(notification).setStatus(NotificationStatus.READ);
        verify(notification).setReadAt(any(OffsetDateTime.class));
        assertEquals(notification, result);
    }

    @Test
    void markAsRead_ShouldThrowExceptionWhenNotificationIsNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            notificationDomainService.markAsRead(null);
        });
    }

    @Test
    void archive_ShouldSetArchivedStatus() {
        // When
        InAppNotification result = notificationDomainService.archive(notification);

        // Then
        verify(notification).setStatus(NotificationStatus.ARCHIVED);
        verify(notification, never()).setReadAt(any());
        verify(notification, never()).setExpiresAt(any());
        assertEquals(notification, result);
    }

    @Test
    void archive_ShouldThrowExceptionWhenNotificationIsNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            notificationDomainService.archive(null);
        });
    }

    @Test
    void expire_ShouldSetExpiredStatusAndExpiresAt() {
        // When
        InAppNotification result = notificationDomainService.expire(notification);

        // Then
        verify(notification).setStatus(NotificationStatus.EXPIRED);
        verify(notification).setExpiresAt(any(OffsetDateTime.class));
        assertEquals(notification, result);
    }

    @Test
    void expire_ShouldThrowExceptionWhenNotificationIsNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            notificationDomainService.expire(null);
        });
    }

    @Test
    void isExpired_ShouldReturnTrueWhenNotificationIsExpired() {
        // Given
        InAppNotification expiredNotification = mock(InAppNotification.class);
        when(expiredNotification.isExpired()).thenReturn(true);

        // When
        boolean result = notificationDomainService.isExpired(expiredNotification);

        // Then
        assertTrue(result);
    }

    @Test
    void isExpired_ShouldReturnFalseWhenNotificationIsNotExpired() {
        // Given
        InAppNotification validNotification = mock(InAppNotification.class);
        when(validNotification.isExpired()).thenReturn(false);

        // When
        boolean result = notificationDomainService.isExpired(validNotification);

        // Then
        assertFalse(result);
    }

    @Test
    void isExpired_ShouldReturnFalseWhenNotificationIsNull() {
        // When
        boolean result = notificationDomainService.isExpired(null);

        // Then
        assertFalse(result);
    }

    @Test
    void allMethods_ShouldReturnSameNotificationInstance() {
        // Given
        InAppNotification testNotification = new InAppNotification();

        // When & Then para initializeNotification
        InAppNotification result1 = notificationDomainService.initializeNotification(testNotification);
        assertSame(testNotification, result1);

        // Cuando la notificación está leída
        testNotification.setStatus(NotificationStatus.READ);
        InAppNotification result2 = notificationDomainService.archive(testNotification);
        assertSame(testNotification, result2);

        // Cuando la notificación está archivada
        InAppNotification result3 = notificationDomainService.expire(testNotification);
        assertSame(testNotification, result3);
    }

    @Test
    void testMethodChaining() {
        // Given
        InAppNotification notification = new InAppNotification();

        // When - Simular un flujo completo
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
