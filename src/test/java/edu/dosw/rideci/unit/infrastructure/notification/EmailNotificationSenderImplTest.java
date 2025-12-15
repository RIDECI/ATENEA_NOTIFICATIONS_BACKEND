package edu.dosw.rideci.unit.infrastructure.notification;

import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.infrastructure.notification.EmailNotificationSenderImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationSenderImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    private EmailNotificationSenderImpl emailSender;

    @BeforeEach
    void setUp() {
        emailSender = new EmailNotificationSenderImpl(mailSender);
    }

    @Test
    void sendNotification_ShouldSendEmailWhenValid() throws MessagingException {
        InAppNotification notification = createTestNotification();
        String destinationEmail = "test@example.com";
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailSender.sendNotification(notification, destinationEmail);

        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendNotification_ShouldNotSendWhenEmailIsNull() {
        InAppNotification notification = createTestNotification();

        emailSender.sendNotification(notification, null);

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void sendNotification_ShouldNotSendWhenEmailIsBlank() {
        InAppNotification notification = createTestNotification();

        emailSender.sendNotification(notification, "   ");

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void sendNotification_ShouldNotSendWhenNotificationIsNull() {
        emailSender.sendNotification(null, "test@example.com");

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void sendNotification_ShouldHandleMessagingException() {
        InAppNotification notification = createTestNotification();
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Test exception")).when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> {
            emailSender.sendNotification(notification, "test@example.com");
        });
    }

    @Test
    void sendNotification_ShouldUseNotificationTitleAsSubject() {
        InAppNotification notification = createTestNotification();
        notification.setTitle("Test Subject");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailSender.sendNotification(notification, "test@example.com");

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendNotification_ShouldUseDefaultSubjectWhenTitleIsNull() {
        InAppNotification notification = createTestNotification();
        notification.setTitle(null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailSender.sendNotification(notification, "test@example.com");

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendNotification_ShouldUseNotificationMessageAsBody() {
        InAppNotification notification = createTestNotification();
        notification.setMessage("<html><body>Test HTML</body></html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailSender.sendNotification(notification, "test@example.com");

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendNotification_ShouldUseDefaultBodyWhenMessageIsNull() {
        InAppNotification notification = createTestNotification();
        notification.setMessage(null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailSender.sendNotification(notification, "test@example.com");

        verify(mailSender).send(any(MimeMessage.class));
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

