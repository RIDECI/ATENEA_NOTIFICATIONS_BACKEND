package edu.dosw.rideci.unit.domain.service;

import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.service.EmailNotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test unitario para la interfaz EmailNotificationSender usando Mockito
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EmailNotificationSender Tests")
class EmailNotificationSenderTest {

        @Mock
        private EmailNotificationSender emailNotificationSender;

        private InAppNotification testNotification;
        private final String TEST_EMAIL = "test@example.com";
        private final String DIFFERENT_EMAIL = "another@example.com";

        @BeforeEach
        void setUp() {
                testNotification = new InAppNotification();
                testNotification.setNotificationId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
                testNotification.setTitle("Test Notification");
                testNotification.setMessage("This is a test notification message");
        }

        @Test
        @DisplayName("Debería enviar notificación con parámetros correctos")
        void shouldSendNotificationWithCorrectParameters() {
                doNothing().when(emailNotificationSender)
                                .sendNotification(any(InAppNotification.class), anyString());

                emailNotificationSender.sendNotification(testNotification, TEST_EMAIL);

                verify(emailNotificationSender, times(1))
                                .sendNotification(testNotification, TEST_EMAIL);
        }

        @Test
        @DisplayName("Debería verificar que se envía una notificación específica")
        void shouldVerifySpecificNotificationIsSent() {
                InAppNotification specificNotification = new InAppNotification();
                specificNotification.setNotificationId(UUID.randomUUID());
                specificNotification.setTitle("Specific Notification");
                specificNotification.setMessage("Specific message");

                emailNotificationSender.sendNotification(specificNotification, TEST_EMAIL);

                verify(emailNotificationSender, times(1))
                                .sendNotification(eq(specificNotification), eq(TEST_EMAIL));
        }

        @Test
        @DisplayName("Debería enviar múltiples notificaciones a diferentes emails")
        void shouldSendMultipleNotificationsToDifferentEmails() {
                emailNotificationSender.sendNotification(testNotification, TEST_EMAIL);
                emailNotificationSender.sendNotification(testNotification, DIFFERENT_EMAIL);

                // Then
                verify(emailNotificationSender, times(2))
                                .sendNotification(any(InAppNotification.class), anyString());

                verify(emailNotificationSender, times(1))
                                .sendNotification(testNotification, TEST_EMAIL);

                verify(emailNotificationSender, times(1))
                                .sendNotification(testNotification, DIFFERENT_EMAIL);
        }

        @Test
        @DisplayName("Debería verificar que no se enviaron notificaciones cuando no se llamó")
        void shouldVerifyNoNotificationsSentWhenNotCalled() {
                verify(emailNotificationSender, never())
                                .sendNotification(any(InAppNotification.class), anyString());
        }

        @Test
        @DisplayName("Debería manejar notificación con email nulo")
        void shouldHandleNotificationWithNullEmail() {
                emailNotificationSender.sendNotification(testNotification, null);

                verify(emailNotificationSender, times(1))
                                .sendNotification(eq(testNotification), isNull());
        }

        @Test
        @DisplayName("Debería manejar notificación nula")
        void shouldHandleNullNotification() {
                emailNotificationSender.sendNotification(null, TEST_EMAIL);

                verify(emailNotificationSender, times(1))
                                .sendNotification(isNull(), eq(TEST_EMAIL));
        }

        @Test
        @DisplayName("Debería verificar el orden de las llamadas")
        void shouldVerifyCallOrder() {
                InAppNotification notification1 = new InAppNotification();
                notification1.setNotificationId(UUID.randomUUID());

                InAppNotification notification2 = new InAppNotification();
                notification2.setNotificationId(UUID.randomUUID());

                emailNotificationSender.sendNotification(notification1, "first@example.com");
                emailNotificationSender.sendNotification(notification2, "second@example.com");

                verify(emailNotificationSender, times(1))
                                .sendNotification(notification1, "first@example.com");

                verify(emailNotificationSender, times(1))
                                .sendNotification(notification2, "second@example.com");
        }

        @Test
        @DisplayName("Debería enviar notificación usando argument captor")
        void shouldSendNotificationUsingArgumentCaptor() {
                org.mockito.ArgumentCaptor<InAppNotification> notificationCaptor = org.mockito.ArgumentCaptor
                                .forClass(InAppNotification.class);

                org.mockito.ArgumentCaptor<String> emailCaptor = org.mockito.ArgumentCaptor.forClass(String.class);

                emailNotificationSender.sendNotification(testNotification, TEST_EMAIL);

                verify(emailNotificationSender, times(1))
                                .sendNotification(notificationCaptor.capture(), emailCaptor.capture());

                InAppNotification capturedNotification = notificationCaptor.getValue();
                String capturedEmail = emailCaptor.getValue();

                assert capturedNotification.getNotificationId().equals(testNotification.getNotificationId());
                assert capturedEmail.equals(TEST_EMAIL);
        }

        @Test
        @DisplayName("Debería verificar número exacto de llamadas")
        void shouldVerifyExactNumberOfCalls() {
                emailNotificationSender.sendNotification(testNotification, TEST_EMAIL);
                emailNotificationSender.sendNotification(testNotification, TEST_EMAIL);
                emailNotificationSender.sendNotification(testNotification, TEST_EMAIL);

                verify(emailNotificationSender, times(3))
                                .sendNotification(testNotification, TEST_EMAIL);

                verify(emailNotificationSender, atLeastOnce())
                                .sendNotification(testNotification, TEST_EMAIL);

                verify(emailNotificationSender, atMost(3))
                                .sendNotification(testNotification, TEST_EMAIL);
        }

        @Test
        @DisplayName("Debería verificar que se llama solo una vez")
        void shouldVerifyCalledOnlyOnce() {
                emailNotificationSender.sendNotification(testNotification, TEST_EMAIL);

                verify(emailNotificationSender, only())
                                .sendNotification(any(InAppNotification.class), anyString());
        }

        @Test
        @DisplayName("Debería resetear el mock y verificar después")
        void shouldResetMockAndVerifyAfter() {
                emailNotificationSender.sendNotification(testNotification, TEST_EMAIL);
                verify(emailNotificationSender, times(1))
                                .sendNotification(testNotification, TEST_EMAIL);

                reset(emailNotificationSender);

                verifyNoInteractions(emailNotificationSender);
        }
}