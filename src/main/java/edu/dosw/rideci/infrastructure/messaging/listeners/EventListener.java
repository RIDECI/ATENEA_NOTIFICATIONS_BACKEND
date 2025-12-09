package edu.dosw.rideci.infrastructure.messaging.listeners;

import edu.dosw.rideci.application.service.EventProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventListener {

    private final EventProcessingService eventProcessingService;

    // ========== LISTENER PARA EVENTOS DE USUARIO ==========

    @RabbitListener(queues = "notif.user.events.queue")
    public void handleUserEvent(edu.dosw.rideci.infrastructure.messaging.events.authentication.UserEvent event) {
        log.info("🎧 [User Queue] Recibido UserEvent: userId={}, eventType={}",
                event.getUserId(), event.getEventType());
        try {
            eventProcessingService.handleUserEvent(event);
        } catch (Exception e) {
            log.error("❌ Error procesando UserEvent: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "notif.user.events.queue")
    public void handlePasswordResetEvent(edu.dosw.rideci.infrastructure.messaging.events.authentication.PasswordResetEvent event) {
        log.info("🎧 [User Queue] Recibido PasswordResetEvent: userId={}", event.getUserId());
        try {
            eventProcessingService.handlePasswordReset(event);
        } catch (Exception e) {
            log.error("❌ Error procesando PasswordResetEvent: {}", e.getMessage(), e);
        }
    }

    // ========== LISTENER PARA EVENTOS DE VIAJES ==========

    @RabbitListener(queues = "notif.travel.events.queue")
    public void handleTravelCreated(edu.dosw.rideci.infrastructure.messaging.events.travel.TravelCreatedEvent event) {
        log.info("🎧 [Travel Queue] Recibido TravelCreatedEvent: travelId={}", event.getTravelId());
        try {
            eventProcessingService.handleTravelCreated(event);
        } catch (Exception e) {
            log.error("❌ Error procesando TravelCreatedEvent: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "notif.travel.events.queue")
    public void handleTravelUpdated(edu.dosw.rideci.infrastructure.messaging.events.travel.TravelUpdatedEvent event) {
        log.info("🎧 [Travel Queue] Recibido TravelUpdatedEvent: travelId={}", event.getTravelId());
        try {
            eventProcessingService.handleTravelUpdated(event);
        } catch (Exception e) {
            log.error("❌ Error procesando TravelUpdatedEvent: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "notif.travel.events.queue")
    public void handleTravelCancelled(edu.dosw.rideci.infrastructure.messaging.events.travel.TravelCancelledEvent event) {
        log.info("🎧 [Travel Queue] Recibido TravelCancelledEvent: travelId={}", event.getTravelId());
        try {
            eventProcessingService.handleTravelCancelled(event);
        } catch (Exception e) {
            log.error("❌ Error procesando TravelCancelledEvent: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "notif.travel.events.queue")
    public void handleTravelCompleted(edu.dosw.rideci.infrastructure.messaging.events.travel.TravelCompletedEvent event) {
        log.info("🎧 [Travel Queue] Recibido TravelCompletedEvent: travelId={}", event.getTravelId());
        try {
            eventProcessingService.handleTravelCompleted(event);
        } catch (Exception e) {
            log.error("❌ Error procesando TravelCompletedEvent: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "notif.travel.events.queue")
    public void handleBookingCreated(edu.dosw.rideci.infrastructure.messaging.events.booking.BookingCreatedEvent event) {
        log.info("🎧 [Travel/Booking Queue] Recibido BookingCreatedEvent: bookingId={}", event.getBookingId());
        try {
            eventProcessingService.handleBookingCreated(event);
        } catch (Exception e) {
            log.error("❌ Error procesando BookingCreatedEvent: {}", e.getMessage(), e);
        }
    }

    // ========== LISTENER PARA EVENTOS DE PAGO ==========

    @RabbitListener(queues = "notif.payment.events.queue")
    public void handlePaymentCompleted(edu.dosw.rideci.infrastructure.messaging.events.payment.PaymentCompletedEvent event) {
        log.info("🎧 [Payment Queue] Recibido PaymentCompletedEvent: paymentId={}", event.getPaymentId());
        try {
            eventProcessingService.handlePaymentCompleted(event);
        } catch (Exception e) {
            log.error("❌ Error procesando PaymentCompletedEvent: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "notif.payment.events.queue")
    public void handlePaymentFailed(edu.dosw.rideci.infrastructure.messaging.events.payment.PaymentFailedEvent event) {
        log.info("🎧 [Payment Queue] Recibido PaymentFailedEvent: paymentId={}", event.getPaymentId());
        try {
            eventProcessingService.handlePaymentFailed(event);
        } catch (Exception e) {
            log.error("❌ Error procesando PaymentFailedEvent: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "notif.payment.events.queue")
    public void handlePaymentRefunded(edu.dosw.rideci.infrastructure.messaging.events.payment.PaymentRefundedEvent event) {
        log.info("🎧 [Payment Queue] Recibido PaymentRefundedEvent: refundId={}", event.getRefundId());
        try {
            eventProcessingService.handlePaymentRefunded(event);
        } catch (Exception e) {
            log.error("❌ Error procesando PaymentRefundedEvent: {}", e.getMessage(), e);
        }
    }

    // ========== LISTENER PARA EVENTOS DE COMUNICACIÓN/REPORTES ==========

    @RabbitListener(queues = "notif.communication.events.queue")
    public void handleReportCreated(edu.dosw.rideci.infrastructure.messaging.events.communication_security.ReportCreatedEvent event) {
        log.info("🎧 [Communication Queue] Recibido ReportCreatedEvent: reportId={}", event.getReportId());
        try {
            eventProcessingService.handleReportCreated(event);
        } catch (Exception e) {
            log.error("❌ Error procesando ReportCreatedEvent: {}", e.getMessage(), e);
        }
    }

    // ========== LISTENERS PARA LAS DEMÁS COLAS ==========

    @RabbitListener(queues = "notif.profile.events.queue")
    public void handleProfileEvents(Object event) {
        log.info("🎧 [Profile Queue] Recibido evento: {}", event.getClass().getSimpleName());
        // Eventos de perfil vendrán aquí
    }

    @RabbitListener(queues = "notif.admin.events.queue")
    public void handleAdminEvents(Object event) {
        log.info("🎧 [Admin Queue] Recibido evento: {}", event.getClass().getSimpleName());
        // Eventos administrativos vendrán aquí
    }
}