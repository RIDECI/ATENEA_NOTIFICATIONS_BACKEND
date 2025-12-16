package edu.dosw.rideci.unit.application.service;

import edu.dosw.rideci.application.port.in.SendEmailNotificationUseCase;
import edu.dosw.rideci.application.service.EmailNotificationApplicationService;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.service.EmailNotificationSender;
import edu.dosw.rideci.domain.service.UserEmailResolver;
import edu.dosw.rideci.infrastructure.notification.EmailTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationApplicationServiceTest {

    @Mock
    private EmailTemplateService templateService;

    @Mock
    private EmailNotificationSender emailSender;

    @Mock
    private UserEmailResolver userEmailResolver;

    private EmailNotificationApplicationService service;

    @BeforeEach
    void setUp() {
        service = new EmailNotificationApplicationService(templateService, emailSender, userEmailResolver);
    }

    @Test
    void send_ShouldSendPasswordRecoveryEmail() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.PASSWORD_RECOVERY)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .reason("RESET123")
                        .build();

        when(templateService.buildPasswordRecoveryEmail(command)).thenReturn("<html>Password recovery</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        assertDoesNotThrow(() -> service.send(command));

        verify(templateService).buildPasswordRecoveryEmail(command);
        verify(emailSender).sendNotification(any(), eq("test@example.com"));
    }

    @Test
    void send_ShouldSendRegistrationVerificationEmail() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.USER_REGISTERED)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .build();

        when(templateService.buildRegistrationVerificationEmail(command)).thenReturn("<html>Registration</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        assertDoesNotThrow(() -> service.send(command));

        verify(templateService).buildRegistrationVerificationEmail(command);
        verify(emailSender).sendNotification(any(), anyString());
    }

    @Test
    void send_ShouldSendDriverVerificationResultEmail() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.DRIVER_VALIDATED)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .build();

        when(templateService.buildDriverVerificationResultEmail(command)).thenReturn("<html>Driver verification</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        assertDoesNotThrow(() -> service.send(command));

        verify(templateService).buildDriverVerificationResultEmail(command);
        verify(emailSender).sendNotification(any(), anyString());
    }

    @Test
    void send_ShouldSendTripBookingConfirmationEmail() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.TRIP_CREATED)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .tripId("trip-123")
                        .build();

        when(templateService.buildTripBookingConfirmationEmail(command)).thenReturn("<html>Trip booking</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        assertDoesNotThrow(() -> service.send(command));

        verify(templateService).buildTripBookingConfirmationEmail(command);
        verify(emailSender).sendNotification(any(), anyString());
    }

    @Test
    void send_ShouldSendTripUpdateEmailForUpdated() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.TRIP_UPDATED)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .build();

        when(templateService.buildTripUpdateEmail(command)).thenReturn("<html>Trip update</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        assertDoesNotThrow(() -> service.send(command));

        verify(templateService).buildTripUpdateEmail(command);
        verify(emailSender).sendNotification(any(), anyString());
    }

    @Test
    void send_ShouldSendTripUpdateEmailForCancelled() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.TRIP_CANCELLED)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .build();

        when(templateService.buildTripUpdateEmail(command)).thenReturn("<html>Trip cancelled</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        assertDoesNotThrow(() -> service.send(command));

        verify(templateService).buildTripUpdateEmail(command);
        verify(emailSender).sendNotification(any(), anyString());
    }

    @Test
    void send_ShouldSendTripReminderEmail() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.UPCOMING_TRIP_REMINDER)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .build();

        when(templateService.buildTripReminderEmail(command)).thenReturn("<html>Trip reminder</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        assertDoesNotThrow(() -> service.send(command));

        verify(templateService).buildTripReminderEmail(command);
        verify(emailSender).sendNotification(any(), anyString());
    }

    @Test
    void send_ShouldSendPaymentConfirmationEmail() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.PAYMENT_CONFIRMED)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .build();

        when(templateService.buildPaymentConfirmationEmail(command)).thenReturn("<html>Payment confirmation</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        assertDoesNotThrow(() -> service.send(command));

        verify(templateService).buildPaymentConfirmationEmail(command);
        verify(emailSender).sendNotification(any(), anyString());
    }

    @Test
    void send_ShouldSendEmergencyAlertEmail() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.EMERGENCY_ALERT)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .build();

        when(templateService.buildEmergencyAlertEmail(command)).thenReturn("<html>Emergency alert</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        assertDoesNotThrow(() -> service.send(command));

        verify(templateService).buildEmergencyAlertEmail(command);
        verify(emailSender).sendNotification(any(), anyString());
    }

    @Test
    void send_ShouldSendAccountSuspensionEmail() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.ACCOUNT_SUSPENDED)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .build();

        when(templateService.buildAccountSuspensionEmail(command)).thenReturn("<html>Account suspension</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        assertDoesNotThrow(() -> service.send(command));

        verify(templateService).buildAccountSuspensionEmail(command);
        verify(emailSender).sendNotification(any(), anyString());
    }

    @Test
    void send_ShouldThrowExceptionForUnsupportedType() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.NOTIFICATION_CREATED)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .build();

        assertThrows(IllegalArgumentException.class, () -> service.send(command));

        verify(emailSender, never()).sendNotification(any(), anyString());
    }

    @Test
    void send_ShouldResolveEmailFromOverride() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.PASSWORD_RECOVERY)
                        .userId(userId)
                        .emailOverride("override@example.com")
                        .build();

        when(templateService.buildPasswordRecoveryEmail(command)).thenReturn("<html>Test</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        service.send(command);

        verify(userEmailResolver, never()).resolveEmail(anyString());
        verify(emailSender).sendNotification(any(), eq("override@example.com"));
    }

    @Test
    void send_ShouldResolveEmailFromUserId() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.PASSWORD_RECOVERY)
                        .userId(userId)
                        .emailOverride(null)
                        .build();

        when(templateService.buildPasswordRecoveryEmail(command)).thenReturn("<html>Test</html>");
        when(userEmailResolver.resolveEmail(userId.toString())).thenReturn("resolved@example.com");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        service.send(command);

        verify(userEmailResolver).resolveEmail(userId.toString());
        verify(emailSender).sendNotification(any(), eq("resolved@example.com"));
    }

    @Test
    void send_ShouldThrowExceptionWhenEmailCannotBeResolved() {
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.PASSWORD_RECOVERY)
                        .userId(null)
                        .emailOverride(null)
                        .build();

        assertThrows(IllegalArgumentException.class, () -> service.send(command));

        verify(templateService, never()).buildPasswordRecoveryEmail(any());
        verify(emailSender, never()).sendNotification(any(), anyString());
    }

    @Test
    void send_ShouldSetCorrectPriorityForEmergencyAlert() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.EMERGENCY_ALERT)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .build();

        when(templateService.buildEmergencyAlertEmail(command)).thenReturn("<html>Emergency</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        service.send(command);

        verify(emailSender).sendNotification(argThat(notification ->
                "HIGH".equals(notification.getPriority())
        ), anyString());
    }

    @Test
    void send_ShouldSetCorrectPriorityForNormalTypes() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.TRIP_CREATED)
                        .userId(userId)
                        .emailOverride("test@example.com")
                        .build();

        when(templateService.buildTripBookingConfirmationEmail(command)).thenReturn("<html>Trip</html>");
        doNothing().when(emailSender).sendNotification(any(), anyString());

        service.send(command);

        verify(emailSender).sendNotification(argThat(notification ->
                "NORMAL".equals(notification.getPriority())
        ), anyString());
    }
}
