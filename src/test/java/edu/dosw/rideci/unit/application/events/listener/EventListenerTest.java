package edu.dosw.rideci.unit.application.events.listener;

import edu.dosw.rideci.application.events.authentication.PasswordResetEvent;
import edu.dosw.rideci.application.events.authentication.UserEvent;
import edu.dosw.rideci.application.events.booking.BookingCreatedEvent;
import edu.dosw.rideci.application.events.communication_security.ReportCreatedEvent;
import edu.dosw.rideci.application.events.payment.PaymentCompletedEvent;
import edu.dosw.rideci.application.events.payment.PaymentFailedEvent;
import edu.dosw.rideci.application.events.payment.PaymentRefundedEvent;
import edu.dosw.rideci.application.events.travel.*;
import edu.dosw.rideci.application.events.listener.EventListener;
import edu.dosw.rideci.application.service.EventProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventListenerTest {

    @Mock
    private EventProcessingService eventProcessingService;

    private EventListener eventListener;

    @BeforeEach
    void setUp() {
        eventListener = new EventListener(eventProcessingService);
    }

    @Test
    void handleUserEvent_ShouldProcessSuccessfully() {
        UserEvent event = new UserEvent();
        event.setUserId(123L);
        event.setEmail("test@example.com");
        event.setName("Test User");

        doNothing().when(eventProcessingService).handleUserEvent(any(UserEvent.class));

        eventListener.handleUserQueue(event);

        verify(eventProcessingService, times(1)).handleUserEvent(event);
    }

    @Test
    void handleUserEvent_ShouldHandleException() {
        UserEvent event = new UserEvent();
        event.setUserId(123L);

        doThrow(new RuntimeException("Test exception"))
                .when(eventProcessingService).handleUserEvent(any(UserEvent.class));

        // Ahora el test espera que se lance la excepción
        assertThrows(RuntimeException.class, () -> eventListener.handleUserQueue(event));

        verify(eventProcessingService, times(1)).handleUserEvent(event);
    }

    @Test
    void handlePasswordResetEvent_ShouldProcessSuccessfully() {
        PasswordResetEvent event = new PasswordResetEvent();
        event.setEmail("test@example.com");
        event.setResetCode("RESET123");

        doNothing().when(eventProcessingService).handlePasswordReset(any(PasswordResetEvent.class));

        eventListener.handleUserQueue(event);

        verify(eventProcessingService, times(1)).handlePasswordReset(event);
    }

    @Test
    void handlePasswordResetEvent_ShouldHandleException() {
        PasswordResetEvent event = new PasswordResetEvent();
        event.setEmail("test@example.com");

        doThrow(new RuntimeException("Test exception"))
                .when(eventProcessingService).handlePasswordReset(any(PasswordResetEvent.class));

        // El test espera la excepción
        assertThrows(RuntimeException.class, () -> eventListener.handleUserQueue(event));

        verify(eventProcessingService, times(1)).handlePasswordReset(event);
    }

    @Test
    void handleTravelCreated_ShouldProcessSuccessfully() {
        TravelCreatedEvent event = new TravelCreatedEvent();
        event.setTravelId("travel-123");

        doNothing().when(eventProcessingService).handleTravelCreated(any(TravelCreatedEvent.class));

        eventListener.handleTravelCreated(event);

        verify(eventProcessingService, times(1)).handleTravelCreated(event);
    }

    @Test
    void handleTravelCreated_ShouldHandleException() {
        TravelCreatedEvent event = new TravelCreatedEvent();
        event.setTravelId("travel-123");

        doThrow(new RuntimeException("Test exception"))
                .when(eventProcessingService).handleTravelCreated(any(TravelCreatedEvent.class));

        eventListener.handleTravelCreated(event);

        verify(eventProcessingService, times(1)).handleTravelCreated(event);
    }

    @Test
    void handleTravelUpdated_ShouldProcessSuccessfully() {
        TravelUpdatedEvent event = new TravelUpdatedEvent();
        event.setTravelId("travel-123");

        doNothing().when(eventProcessingService).handleTravelUpdated(any(TravelUpdatedEvent.class));

        eventListener.handleTravelUpdated(event);

        verify(eventProcessingService, times(1)).handleTravelUpdated(event);
    }

    @Test
    void handleTravelUpdated_ShouldHandleException() {
        TravelUpdatedEvent event = new TravelUpdatedEvent();
        event.setTravelId("travel-123");

        doThrow(new RuntimeException("Test exception"))
                .when(eventProcessingService).handleTravelUpdated(any(TravelUpdatedEvent.class));

        eventListener.handleTravelUpdated(event);

        verify(eventProcessingService, times(1)).handleTravelUpdated(event);
    }

    @Test
    void handleTravelCancelled_ShouldProcessSuccessfully() {
        TravelCancelledEvent event = new TravelCancelledEvent();
        event.setTravelId("travel-123");

        doNothing().when(eventProcessingService).handleTravelCancelled(any(TravelCancelledEvent.class));

        eventListener.handleTravelCancelled(event);

        verify(eventProcessingService, times(1)).handleTravelCancelled(event);
    }

    @Test
    void handleTravelCancelled_ShouldHandleException() {
        TravelCancelledEvent event = new TravelCancelledEvent();
        event.setTravelId("travel-123");

        doThrow(new RuntimeException("Test exception"))
                .when(eventProcessingService).handleTravelCancelled(any(TravelCancelledEvent.class));

        eventListener.handleTravelCancelled(event);

        verify(eventProcessingService, times(1)).handleTravelCancelled(event);
    }

    @Test
    void handleTravelCompleted_ShouldProcessSuccessfully() {
        TravelCompletedEvent event = new TravelCompletedEvent();
        event.setTravelId("travel-123");

        doNothing().when(eventProcessingService).handleTravelCompleted(any(TravelCompletedEvent.class));

        eventListener.handleTravelCompleted(event);

        verify(eventProcessingService, times(1)).handleTravelCompleted(event);
    }

    @Test
    void handleTravelCompleted_ShouldHandleException() {
        TravelCompletedEvent event = new TravelCompletedEvent();
        event.setTravelId("travel-123");

        doThrow(new RuntimeException("Test exception"))
                .when(eventProcessingService).handleTravelCompleted(any(TravelCompletedEvent.class));

        eventListener.handleTravelCompleted(event);

        verify(eventProcessingService, times(1)).handleTravelCompleted(event);
    }

    @Test
    void handleBookingCreated_ShouldProcessSuccessfully() {
        BookingCreatedEvent event = new BookingCreatedEvent();
        event.setBookingId("booking-123");

        doNothing().when(eventProcessingService).handleBookingCreated(any(BookingCreatedEvent.class));

        eventListener.handleBookingCreated(event);

        verify(eventProcessingService, times(1)).handleBookingCreated(event);
    }

    @Test
    void handleBookingCreated_ShouldHandleException() {
        BookingCreatedEvent event = new BookingCreatedEvent();
        event.setBookingId("booking-123");

        doThrow(new RuntimeException("Test exception"))
                .when(eventProcessingService).handleBookingCreated(any(BookingCreatedEvent.class));

        eventListener.handleBookingCreated(event);

        verify(eventProcessingService, times(1)).handleBookingCreated(event);
    }

    @Test
    void handlePaymentCompleted_ShouldProcessSuccessfully() {
        PaymentCompletedEvent event = new PaymentCompletedEvent();
        event.setPaymentId("payment-123");

        doNothing().when(eventProcessingService).handlePaymentCompleted(any(PaymentCompletedEvent.class));

        eventListener.handlePaymentCompleted(event);

        verify(eventProcessingService, times(1)).handlePaymentCompleted(event);
    }

    @Test
    void handlePaymentCompleted_ShouldHandleException() {
        PaymentCompletedEvent event = new PaymentCompletedEvent();
        event.setPaymentId("payment-123");

        doThrow(new RuntimeException("Test exception"))
                .when(eventProcessingService).handlePaymentCompleted(any(PaymentCompletedEvent.class));

        eventListener.handlePaymentCompleted(event);

        verify(eventProcessingService, times(1)).handlePaymentCompleted(event);
    }

    @Test
    void handlePaymentFailed_ShouldProcessSuccessfully() {
        PaymentFailedEvent event = new PaymentFailedEvent();
        event.setPaymentId("payment-123");

        doNothing().when(eventProcessingService).handlePaymentFailed(any(PaymentFailedEvent.class));

        eventListener.handlePaymentFailed(event);

        verify(eventProcessingService, times(1)).handlePaymentFailed(event);
    }

    @Test
    void handlePaymentFailed_ShouldHandleException() {
        PaymentFailedEvent event = new PaymentFailedEvent();
        event.setPaymentId("payment-123");

        doThrow(new RuntimeException("Test exception"))
                .when(eventProcessingService).handlePaymentFailed(any(PaymentFailedEvent.class));

        eventListener.handlePaymentFailed(event);

        verify(eventProcessingService, times(1)).handlePaymentFailed(event);
    }

    @Test
    void handlePaymentRefunded_ShouldProcessSuccessfully() {
        PaymentRefundedEvent event = new PaymentRefundedEvent();
        event.setRefundId("refund-123");

        doNothing().when(eventProcessingService).handlePaymentRefunded(any(PaymentRefundedEvent.class));

        eventListener.handlePaymentRefunded(event);

        verify(eventProcessingService, times(1)).handlePaymentRefunded(event);
    }

    @Test
    void handlePaymentRefunded_ShouldHandleException() {
        PaymentRefundedEvent event = new PaymentRefundedEvent();
        event.setRefundId("refund-123");

        doThrow(new RuntimeException("Test exception"))
                .when(eventProcessingService).handlePaymentRefunded(any(PaymentRefundedEvent.class));

        eventListener.handlePaymentRefunded(event);

        verify(eventProcessingService, times(1)).handlePaymentRefunded(event);
    }

    @Test
    void handleReportCreated_ShouldProcessSuccessfully() {
        ReportCreatedEvent event = new ReportCreatedEvent();
        event.setReportId("report-123");

        doNothing().when(eventProcessingService).handleReportCreated(any(ReportCreatedEvent.class));

        eventListener.handleReportCreated(event);

        verify(eventProcessingService, times(1)).handleReportCreated(event);
    }

    @Test
    void handleReportCreated_ShouldHandleException() {
        ReportCreatedEvent event = new ReportCreatedEvent();
        event.setReportId("report-123");

        doThrow(new RuntimeException("Test exception"))
                .when(eventProcessingService).handleReportCreated(any(ReportCreatedEvent.class));

        eventListener.handleReportCreated(event);

        verify(eventProcessingService, times(1)).handleReportCreated(event);
    }

    @Test
    void handleProfileEvents_ShouldLogEvent() {
        Object event = new Object();

        eventListener.handleProfileEvents(event);

        verify(eventProcessingService, never()).handleUserEvent(any());
    }

    @Test
    void handleAdminEvents_ShouldLogEvent() {
        Object event = new Object();

        eventListener.handleAdminEvents(event);

        verify(eventProcessingService, never()).handleUserEvent(any());
    }
}

