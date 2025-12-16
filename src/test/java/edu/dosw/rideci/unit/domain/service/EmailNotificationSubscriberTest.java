package edu.dosw.rideci.unit.domain.service;

import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.NotificationEvent;
import edu.dosw.rideci.domain.service.EmailNotificationSender;
import edu.dosw.rideci.domain.service.EmailNotificationSubscriber;
import edu.dosw.rideci.domain.service.EventBus;
import edu.dosw.rideci.domain.service.UserEmailResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationSubscriberTest {

    @Mock
    private EventBus eventBus;

    @Mock
    private EmailNotificationSender emailNotificationSender;

    @Mock
    private UserEmailResolver userEmailResolver;

    @InjectMocks
    private EmailNotificationSubscriber emailNotificationSubscriber;

    private NotificationEvent testEvent;
    private InAppNotification testNotification;
    private final UUID TEST_USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final UUID TEST_NOTIFICATION_ID = UUID.fromString("223e4567-e89b-12d3-a456-426614174001");
    private final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        testNotification = new InAppNotification();
        testNotification.setNotificationId(TEST_NOTIFICATION_ID);
        testNotification.setUserId(TEST_USER_ID);
        testNotification.setTitle("Test Notification");
        testNotification.setMessage("Test message");

        testEvent = new NotificationEvent(
                "event-id-123",
                NotificationType.NOTIFICATION_CREATED,
                "aggregateId",
                "aggregateType",
                "correlationId",
                1,
                Instant.now(),
                "test",
                testNotification
        );
    }

    @Test
    void shouldRegisterSuccessfullyOnInitialization() {
        emailNotificationSubscriber.register();
        verify(eventBus, times(1)).subscribe(NotificationType.NOTIFICATION_CREATED, emailNotificationSubscriber);
    }

    @Test
    void shouldHandleEventAndSendEmailWhenEverythingValid() {
        when(userEmailResolver.resolveEmail(TEST_USER_ID.toString())).thenReturn(TEST_EMAIL);
        emailNotificationSubscriber.handleEvent(testEvent);
        verify(userEmailResolver, times(1)).resolveEmail(TEST_USER_ID.toString());
        verify(emailNotificationSender, times(1)).sendNotification(testNotification, TEST_EMAIL);
    }

    @Test
    void shouldLogWarningWhenNotificationIsNull() {
        NotificationEvent eventWithNullNotification = new NotificationEvent(
                "event-id-124",
                NotificationType.NOTIFICATION_CREATED,
                "aggregateId",
                "aggregateType",
                "correlationId",
                1,
                Instant.now(),
                "test",
                null
        );
        emailNotificationSubscriber.handleEvent(eventWithNullNotification);
        verify(userEmailResolver, never()).resolveEmail(anyString());
        verify(emailNotificationSender, never()).sendNotification(any(), anyString());
    }

    @Test
    void shouldLogWarningWhenUserIdIsNull() {
        InAppNotification notificationWithoutUserId = new InAppNotification();
        notificationWithoutUserId.setNotificationId(TEST_NOTIFICATION_ID);
        notificationWithoutUserId.setUserId(null);

        NotificationEvent event = new NotificationEvent(
                "event-id-125",
                NotificationType.NOTIFICATION_CREATED,
                "aggregateId",
                "aggregateType",
                "correlationId",
                1,
                Instant.now(),
                "test",
                notificationWithoutUserId
        );
        emailNotificationSubscriber.handleEvent(event);
        verify(userEmailResolver, never()).resolveEmail(anyString());
        verify(emailNotificationSender, never()).sendNotification(any(), anyString());
    }

    @Test
    void shouldLogWarningWhenEmailCannotBeResolved() {
        when(userEmailResolver.resolveEmail(TEST_USER_ID.toString())).thenReturn(null);
        emailNotificationSubscriber.handleEvent(testEvent);
        verify(userEmailResolver, times(1)).resolveEmail(TEST_USER_ID.toString());
        verify(emailNotificationSender, never()).sendNotification(any(), anyString());
    }

    @Test
    void shouldLogWarningWhenEmailIsEmpty() {
        when(userEmailResolver.resolveEmail(TEST_USER_ID.toString())).thenReturn("");
        emailNotificationSubscriber.handleEvent(testEvent);
        verify(userEmailResolver, times(1)).resolveEmail(TEST_USER_ID.toString());
        verify(emailNotificationSender, never()).sendNotification(any(), anyString());
    }

    @Test
    void shouldHandleExceptionsDuringEmailSending() {
        when(userEmailResolver.resolveEmail(TEST_USER_ID.toString())).thenReturn(TEST_EMAIL);
        doThrow(new RuntimeException("SMTP Error")).when(emailNotificationSender).sendNotification(any(), anyString());
        emailNotificationSubscriber.handleEvent(testEvent);
        verify(userEmailResolver, times(1)).resolveEmail(TEST_USER_ID.toString());
        verify(emailNotificationSender, times(1)).sendNotification(any(), anyString());
    }

    @Test
    void shouldGetSubscribedEventsCorrectly() {
        List<NotificationType> subscribedEvents = emailNotificationSubscriber.getSubscribedEvents();
        assertEquals(1, subscribedEvents.size());
        assertEquals(NotificationType.NOTIFICATION_CREATED, subscribedEvents.get(0));
    }

    @Test
    void shouldGetCorrectName() {
        String name = emailNotificationSubscriber.getName();
        assertEquals("email-notification-subscriber", name);
    }

    @Test
    void shouldProcessMultipleEventsCorrectly() {
        when(userEmailResolver.resolveEmail(TEST_USER_ID.toString())).thenReturn(TEST_EMAIL);
        NotificationEvent event1 = testEvent;
        NotificationEvent event2 = new NotificationEvent(
                "event-id-126",
                NotificationType.NOTIFICATION_CREATED,
                "aggregateId",
                "aggregateType",
                "correlationId",
                1,
                Instant.now(),
                "test",
                testNotification
        );
        emailNotificationSubscriber.handleEvent(event1);
        emailNotificationSubscriber.handleEvent(event2);
        verify(userEmailResolver, times(2)).resolveEmail(TEST_USER_ID.toString());
        verify(emailNotificationSender, times(2)).sendNotification(testNotification, TEST_EMAIL);
    }

    @Test
    void shouldUseArgumentCaptorToVerifySendingParameters() {
        when(userEmailResolver.resolveEmail(TEST_USER_ID.toString())).thenReturn(TEST_EMAIL);
        ArgumentCaptor<InAppNotification> notificationCaptor = ArgumentCaptor.forClass(InAppNotification.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        emailNotificationSubscriber.handleEvent(testEvent);
        verify(emailNotificationSender, times(1)).sendNotification(notificationCaptor.capture(), emailCaptor.capture());
        InAppNotification capturedNotification = notificationCaptor.getValue();
        String capturedEmail = emailCaptor.getValue();
        assertEquals(TEST_NOTIFICATION_ID, capturedNotification.getNotificationId());
        assertEquals(TEST_USER_ID, capturedNotification.getUserId());
        assertEquals(TEST_EMAIL, capturedEmail);
    }
}
