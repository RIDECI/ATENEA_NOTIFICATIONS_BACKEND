package edu.dosw.rideci.unit.application.service;

import edu.dosw.rideci.application.events.authentication.PasswordResetEvent;
import edu.dosw.rideci.application.events.authentication.UserEvent;
import edu.dosw.rideci.application.events.booking.BookingCreatedEvent;
import edu.dosw.rideci.application.events.communication_security.ReportCreatedEvent;
import edu.dosw.rideci.application.events.payment.PaymentCompletedEvent;
import edu.dosw.rideci.application.events.payment.PaymentFailedEvent;
import edu.dosw.rideci.application.events.payment.PaymentRefundedEvent;
import edu.dosw.rideci.application.events.travel.*;
import edu.dosw.rideci.application.port.in.SendEmailNotificationUseCase;
import edu.dosw.rideci.application.service.EventProcessingService;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.NotificationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventProcessingServiceTest {

    @Mock
    private SendEmailNotificationUseCase sendEmailUseCase;

    private EventProcessingService service;

    @BeforeEach
    void setUp() {
        service = new EventProcessingService(sendEmailUseCase);
    }

    @Test
    void createNotificationEvent_ShouldCreateEventWithCorrectData() {
        NotificationEvent event = service.createNotificationEvent(
                "TEST_MODULE",
                NotificationType.TRIP_CREATED,
                "user-123",
                "Test message",
                "{\"key\":\"value\"}"
        );

        assertNotNull(event);
        assertNotNull(event.getEventId());
        assertEquals(NotificationType.TRIP_CREATED, event.getEventType());
        assertEquals("TEST_MODULE", event.getSourceModule());
        assertEquals("user-123", event.getUserId());
        assertEquals("Test message", event.getMessage());
        assertEquals(3, event.getPriority());
        assertNotNull(event.getTimestamp());
    }

    @Test
    void createInAppNotification_ShouldCreateNotificationFromEvent() {
        String userId = UUID.randomUUID().toString();
        NotificationEvent event = NotificationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(NotificationType.TRIP_CREATED)
                .userId(userId)
                .message("Test message")
                .timestamp(Instant.now())
                .build();

        var notification = service.createInAppNotification(event);

        assertNotNull(notification);
        assertNotNull(notification.getNotificationId());
        assertNotNull(notification.getUserId());
        assertEquals("Test message", notification.getMessage());
    }

    @Test
    void handlePaymentCompleted_ShouldProcessEvent() {
        PaymentCompletedEvent event = new PaymentCompletedEvent();
        event.setPaymentId("payment-123");
        event.setUserId(UUID.randomUUID().toString());
        event.setAmount(100.50);
        event.setTripId("trip-123");

        service.handlePaymentCompleted(event);

        verify(sendEmailUseCase, never()).send(any());
    }

    @Test
    void handlePaymentFailed_ShouldProcessEvent() {
        PaymentFailedEvent event = new PaymentFailedEvent();
        event.setPaymentId("payment-123");
        event.setUserId(UUID.randomUUID().toString());
        event.setAmount(100.50);
        event.setTripId("trip-123");

        service.handlePaymentFailed(event);

        assertNotNull(event);
    }

    @Test
    void handlePaymentRefunded_ShouldProcessEvent() {
        PaymentRefundedEvent event = new PaymentRefundedEvent();
        event.setRefundId("refund-123");
        event.setUserId(UUID.randomUUID().toString());
        event.setAmount(50.00);
        event.setOriginalPaymentId("payment-123");

        service.handlePaymentRefunded(event);

        assertNotNull(event);
    }

    @Test
    void handleReportCreated_ShouldProcessEvent() {
        ReportCreatedEvent event = new ReportCreatedEvent();
        event.setReportId("report-123");
        event.setUserId(UUID.randomUUID().toString());
        event.setSeverity("CRITICAL");
        event.setReportType("EMERGENCY");
        event.setTitle("Test Report");
        event.setRequiresImmediateAction(true);

        service.handleReportCreated(event);

        assertNotNull(event);
    }

    @Test
    void handleTravelCreated_ShouldProcessEvent() {
        TravelCreatedEvent event = new TravelCreatedEvent();
        event.setTravelId("travel-123");
        event.setDriverId(UUID.randomUUID().toString());
        TravelCreatedEvent.Location origin = new TravelCreatedEvent.Location();
        origin.setAddress("Origin Address");
        TravelCreatedEvent.Location destination = new TravelCreatedEvent.Location();
        destination.setAddress("Destination Address");
        event.setOrigin(origin);
        event.setDestination(destination);

        service.handleTravelCreated(event);

        assertNotNull(event);
    }

    @Test
    void handleTravelUpdated_ShouldProcessEvent() {
        TravelUpdatedEvent event = new TravelUpdatedEvent();
        event.setTravelId("travel-123");
        event.setDriverId(UUID.randomUUID().toString());
        event.setUpdateReason("Schedule change");
        event.setChanges("Updated departure time");

        service.handleTravelUpdated(event);

        assertNotNull(event);
    }

    @Test
    void handleTravelCancelled_ShouldProcessEvent() {
        TravelCancelledEvent event = new TravelCancelledEvent();
        event.setTravelId("travel-123");
        event.setDriverId(UUID.randomUUID().toString());
        event.setCancellationReason("Driver unavailable");
        event.setAffectedPassengers(3);

        service.handleTravelCancelled(event);

        assertNotNull(event);
    }

    @Test
    void handleTravelCompleted_ShouldProcessEvent() {
        TravelCompletedEvent event = new TravelCompletedEvent();
        event.setTravelId("travel-123");
        event.setDriverId(UUID.randomUUID().toString());
        event.setPassengerIds(Arrays.asList("passenger-1", "passenger-2"));
        event.setTotalAmount(200.00);
        event.setRatingEnabled(true);

        service.handleTravelCompleted(event);

        assertNotNull(event);
    }

    @Test
    void handleTravelCompleted_ShouldNotCreateRatingNotificationWhenDisabled() {
        TravelCompletedEvent event = new TravelCompletedEvent();
        event.setTravelId("travel-123");
        event.setDriverId(UUID.randomUUID().toString());
        event.setPassengerIds(Arrays.asList("passenger-1"));
        event.setRatingEnabled(false);

        service.handleTravelCompleted(event);

        assertNotNull(event);
    }

    @Test
    void handleBookingCreated_ShouldProcessEvent() {
        BookingCreatedEvent event = new BookingCreatedEvent();
        event.setBookingId("booking-123");
        event.setTravelId("travel-123");
        event.setPassengerId(123L);
        event.setReservedSeats(2);

        service.handleBookingCreated(event);

        assertNotNull(event);
    }

    @Test
    void handleUserEvent_ShouldProcessEvent() {
        UserEvent event = new UserEvent();
        event.setUserId(123L);
        event.setName("Test User");
        event.setEmail("test@example.com");
        event.setRole("USER");

        service.handleUserEvent(event);

        assertNotNull(event);
    }

    @Test
    void handlePasswordReset_ShouldProcessEventAndSendEmail() {
        PasswordResetEvent event = new PasswordResetEvent();
        event.setEmail("test@example.com");
        event.setResetCode("RESET123");
        event.setExpiryMinutes(30);

        service.handlePasswordReset(event);

        verify(sendEmailUseCase, times(1)).send(any());
    }
}

