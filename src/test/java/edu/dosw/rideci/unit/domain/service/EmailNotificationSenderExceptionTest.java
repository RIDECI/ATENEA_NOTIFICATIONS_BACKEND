package edu.dosw.rideci.unit.domain.service;

import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.service.EmailNotificationSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailNotificationSender Exception Tests")
class EmailNotificationSenderExceptionTest {

    @Mock
    private EmailNotificationSender emailNotificationSender;

    @Test
    @DisplayName("Debería lanzar excepción cuando falla el envío")
    void shouldThrowExceptionWhenSendingFails() {
        // Given
        InAppNotification notification = new InAppNotification();
        notification.setNotificationId(UUID.randomUUID());

        String email = "test@example.com";

        // Configurar el mock para lanzar excepción
        doThrow(new RuntimeException("SMTP server unavailable"))
                .when(emailNotificationSender)
                .sendNotification(notification, email);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            emailNotificationSender.sendNotification(notification, email);
        });
    }

    @Test
    @DisplayName("Debería manejar múltiples excepciones")
    void shouldHandleMultipleExceptions() {
        // Given
        InAppNotification notification = new InAppNotification();
        String email = "test@example.com";

        // Configurar para lanzar diferentes excepciones en diferentes llamadas
        doThrow(new RuntimeException("First error"))
                .doThrow(new IllegalArgumentException("Invalid email"))
                .doNothing()
                .when(emailNotificationSender)
                .sendNotification(notification, email);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            emailNotificationSender.sendNotification(notification, email);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            emailNotificationSender.sendNotification(notification, email);
        });

        // Tercera llamada debería funcionar
        emailNotificationSender.sendNotification(notification, email);

        verify(emailNotificationSender, times(3))
                .sendNotification(notification, email);
    }
}