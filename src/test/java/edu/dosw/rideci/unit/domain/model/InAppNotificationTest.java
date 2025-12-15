package edu.dosw.rideci.unit.domain.model;

import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.InAppNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InAppNotificationTest {

    private InAppNotification notification;
    private UUID notificationId;
    private UUID userId;
    private OffsetDateTime createdAt;
    private OffsetDateTime expiresAt;

    @BeforeEach
    void setUp() {
        notificationId = UUID.randomUUID();
        userId = UUID.randomUUID();
        createdAt = OffsetDateTime.now();
        expiresAt = createdAt.plusDays(1);

        notification = InAppNotification.builder()
                .notificationId(notificationId)
                .userId(userId)
                .title("Test Notification")
                .message("This is a test message")
                .eventType(NotificationType.USER_REGISTERED)
                .priority("NORMAL")
                .status(NotificationStatus.UNREAD)
                .createdAt(createdAt)
                .expiresAt(expiresAt)
                .build();
    }

    @Test
    @DisplayName("Debería crear correctamente una notificación con todos los campos")
    void shouldCreateNotificationWithAllFields() {
        assertThat(notification.getNotificationId()).isEqualTo(notificationId);
        assertThat(notification.getUserId()).isEqualTo(userId);
        assertThat(notification.getTitle()).isEqualTo("Test Notification");
        assertThat(notification.getMessage()).isEqualTo("This is a test message");
        assertThat(notification.getEventType()).isEqualTo(NotificationType.USER_REGISTERED);
        assertThat(notification.getPriority()).isEqualTo("NORMAL");
        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.UNREAD);
        assertThat(notification.getCreatedAt()).isEqualTo(createdAt);
        assertThat(notification.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(notification.getReadAt()).isNull();
    }

    @Test
    @DisplayName("Debería marcar la notificación como leída y establecer readAt")
    void shouldMarkNotificationAsRead() {
        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.UNREAD);
        assertThat(notification.getReadAt()).isNull();

        notification.markAsRead();

        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.READ);
        assertThat(notification.getReadAt()).isNotNull();
        assertThat(notification.getReadAt()).isBeforeOrEqualTo(OffsetDateTime.now());
    }

    @Test
    @DisplayName("Debería retornar true cuando la notificación está expirada")
    void shouldReturnTrueWhenNotificationIsExpired() {
        OffsetDateTime pastExpiryDate = OffsetDateTime.now().minusDays(1);
        notification.setExpiresAt(pastExpiryDate);

        assertThat(notification.isExpired()).isTrue();
    }

    @Test
    @DisplayName("Debería retornar false cuando la notificación no está expirada")
    void shouldReturnFalseWhenNotificationIsNotExpired() {
        OffsetDateTime futureExpiryDate = OffsetDateTime.now().plusDays(1);
        notification.setExpiresAt(futureExpiryDate);

        assertThat(notification.isExpired()).isFalse();
    }

    @Test
    @DisplayName("Debería retornar false cuando expiresAt es null")
    void shouldReturnFalseWhenExpiresAtIsNull() {
        notification.setExpiresAt(null);

        assertThat(notification.isExpired()).isFalse();
    }

    @Test
    @DisplayName("Debería retornar el mensaje de visualización correcto")
    void shouldReturnCorrectDisplayMessage() {
        String expectedDisplayMessage = "Test Notification - This is a test message";

        assertThat(notification.getDisplayMessage()).isEqualTo(expectedDisplayMessage);
    }

    @Test
    @DisplayName("Debería retornar el ID correcto con el método getId")
    void shouldReturnCorrectIdWithGetIdMethod() {
        // SOLUCIÓN: Comparar con el UUID directamente, no con su representación string
        assertThat(notification.getId()).isEqualTo(notificationId);
    }

    @Test
    @DisplayName("Debería poder actualizar los campos de la notificación")
    void shouldUpdateNotificationFields() {
        notification.setTitle("Updated Title");
        notification.setMessage("Updated Message");
        notification.setPriority("HIGH");
        notification.setStatus(NotificationStatus.READ);
        notification.setReadAt(OffsetDateTime.now());

        assertThat(notification.getTitle()).isEqualTo("Updated Title");
        assertThat(notification.getMessage()).isEqualTo("Updated Message");
        assertThat(notification.getPriority()).isEqualTo("HIGH");
        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.READ);
        assertThat(notification.getReadAt()).isNotNull();
    }

    @Test
    @DisplayName("Debería poder crear notificación usando constructor con todos los argumentos")
    void shouldCreateNotificationWithAllArgsConstructor() {
        OffsetDateTime readAt = OffsetDateTime.now();
        InAppNotification newNotification = new InAppNotification(
                notificationId,
                userId,
                "Constructor Title",
                "Constructor Message",
                NotificationType.PASSWORD_RECOVERY,
                "HIGH",
                NotificationStatus.READ,
                createdAt,
                readAt,
                expiresAt
        );

        assertThat(newNotification.getNotificationId()).isEqualTo(notificationId);
        assertThat(newNotification.getUserId()).isEqualTo(userId);
        assertThat(newNotification.getTitle()).isEqualTo("Constructor Title");
        assertThat(newNotification.getMessage()).isEqualTo("Constructor Message");
        assertThat(newNotification.getEventType()).isEqualTo(NotificationType.PASSWORD_RECOVERY);
        assertThat(newNotification.getPriority()).isEqualTo("HIGH");
        assertThat(newNotification.getStatus()).isEqualTo(NotificationStatus.READ);
        assertThat(newNotification.getCreatedAt()).isEqualTo(createdAt);
        assertThat(newNotification.getReadAt()).isEqualTo(readAt);
        assertThat(newNotification.getExpiresAt()).isEqualTo(expiresAt);
    }

    @Test
    @DisplayName("Debería poder crear notificación usando constructor sin argumentos")
    void shouldCreateNotificationWithNoArgsConstructor() {
        InAppNotification newNotification = new InAppNotification();

        assertThat(newNotification.getNotificationId()).isNull();
        assertThat(newNotification.getUserId()).isNull();
        assertThat(newNotification.getTitle()).isNull();
        assertThat(newNotification.getMessage()).isNull();
        assertThat(newNotification.getEventType()).isNull();
        assertThat(newNotification.getPriority()).isNull();
        assertThat(newNotification.getStatus()).isNull();
        assertThat(newNotification.getCreatedAt()).isNull();
        assertThat(newNotification.getReadAt()).isNull();
        assertThat(newNotification.getExpiresAt()).isNull();
    }

    @Test
    @DisplayName("Debería mostrar mensaje de visualización incluso con campos nulos")
    void shouldDisplayMessageEvenWithNullFields() {
        InAppNotification newNotification = new InAppNotification();
        newNotification.setTitle(null);
        newNotification.setMessage(null);

        String displayMessage = newNotification.getDisplayMessage();

        assertThat(displayMessage).isEqualTo("null - null");
    }

    @Test
    @DisplayName("Debería manejar correctamente diferentes tipos de notificaciones")
    void shouldHandleDifferentNotificationTypes() {
        NotificationType[] types = NotificationType.values();

        for (NotificationType type : types) {
            notification.setEventType(type);
            assertThat(notification.getEventType()).isEqualTo(type);
        }
    }

    @Test
    @DisplayName("Debería manejar correctamente diferentes estados de notificación")
    void shouldHandleDifferentNotificationStatuses() {
        NotificationStatus[] statuses = NotificationStatus.values();

        for (NotificationStatus status : statuses) {
            notification.setStatus(status);
            assertThat(notification.getStatus()).isEqualTo(status);
        }
    }
}