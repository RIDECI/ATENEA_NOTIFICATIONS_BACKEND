package edu.dosw.rideci.unit.infrastructure.controller;

import edu.dosw.rideci.application.port.in.SendEmailNotificationUseCase;
import edu.dosw.rideci.infrastructure.controller.EmailNotificationController;
import edu.dosw.rideci.infrastructure.controller.dto.Request.EmailNotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationControllerTest {

    @Mock
    private SendEmailNotificationUseCase sendEmailUseCase;

    private EmailNotificationController controller;

    @BeforeEach
    void setUp() {
        controller = new EmailNotificationController(sendEmailUseCase);
    }

    @Test
    void sendPasswordRecovery_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                null,
                null,
                null,
                "Reset reason",
                "Extra info",
                OffsetDateTime.now()
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendPasswordRecovery(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNull(response.getBody());
        verify(sendEmailUseCase).send(any());
    }

    @Test
    void sendRegistrationVerification_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                null,
                null,
                null,
                null,
                null,
                null
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendRegistrationVerification(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(sendEmailUseCase).send(any());
    }

    @Test
    void sendDriverVerificationResult_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                null,
                "driver-123",
                null,
                "Verification reason",
                null,
                null
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendDriverVerificationResult(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(sendEmailUseCase).send(any());
    }

    @Test
    void sendTripBookingConfirmation_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                "trip-123",
                null,
                null,
                "Booking reason",
                null,
                null
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendTripBookingConfirmation(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(sendEmailUseCase).send(any());
    }

    @Test
    void sendTravelCreatedNotification_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                null,
                null,
                null,
                null,
                "Extra info",
                null
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendTravelCreatedNotification(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(sendEmailUseCase).send(any());
    }

    @Test
    void sendTravelUpdatedNotification_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                null,
                null,
                null,
                "Update reason",
                "Extra info",
                null
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendTravelUpdatedNotification(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(sendEmailUseCase).send(any());
    }

    @Test
    void sendTripUpdate_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                "trip-123",
                null,
                null,
                "Update reason",
                null,
                null
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendTripUpdate(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(sendEmailUseCase).send(any());
    }

    @Test
    void sendTripReminder_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                "trip-123",
                null,
                null,
                "Reminder reason",
                null,
                null
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendTripReminder(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(sendEmailUseCase).send(any());
    }

    @Test
    void sendTravelCompletedNotification_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                null,
                null,
                null,
                null,
                "Extra info",
                null
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendTravelCompletedNotification(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(sendEmailUseCase).send(any());
    }

    @Test
    void sendTravelCancelledNotification_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                null,
                null,
                null,
                "Cancellation reason",
                "Extra info",
                null
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendTravelCancelledNotification(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(sendEmailUseCase).send(any());
    }

    @Test
    void sendPaymentConfirmation_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                "trip-123",
                null,
                "payment-123",
                "Payment reason",
                null,
                null
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendPaymentConfirmation(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(sendEmailUseCase).send(any());
    }

    @Test
    void sendAccountSuspension_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                null,
                null,
                null,
                "Suspension reason",
                "Extra info",
                null
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendAccountSuspension(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(sendEmailUseCase).send(any());
    }

    @Test
    void sendEmergencyAlert_ShouldReturnAccepted() {
        EmailNotificationRequest request = new EmailNotificationRequest(
                UUID.randomUUID(),
                "test@example.com",
                "trip-123",
                null,
                null,
                "Emergency reason",
                "Extra info",
                null
        );

        doNothing().when(sendEmailUseCase).send(any());

        ResponseEntity<Void> response = controller.sendEmergencyAlert(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(sendEmailUseCase).send(any());
    }
}

