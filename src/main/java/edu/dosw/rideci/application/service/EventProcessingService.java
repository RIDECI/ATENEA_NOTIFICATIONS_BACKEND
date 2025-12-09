package edu.dosw.rideci.application.service;

import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.NotificationEvent;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventProcessingService {

    // Método para crear un NotificationEvent a partir de cualquier evento
    public NotificationEvent createNotificationEvent(String sourceModule, NotificationType type,
                                                     String userId, String message, String payload) {
        return NotificationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(type)
                .sourceModule(sourceModule)
                .userId(userId)
                .message(message)
                .priority(3) // Prioridad media por defecto
                .timestamp(Instant.now())
                .payload(payload)
                .build();
    }

    // Método para crear InAppNotification a partir de un NotificationEvent
    public InAppNotification createInAppNotification(NotificationEvent event) {
        return InAppNotification.builder()
                .notificationId(UUID.randomUUID())
                .userId(UUID.fromString(event.getUserId()))
                .title("Notificación del Sistema")
                .message(event.getMessage())
                .eventType(event.getEventType())
                .priority("NORMAL")
                .status(NotificationStatus.UNREAD)
                .createdAt(OffsetDateTime.now())
                .expiresAt(OffsetDateTime.now().plusDays(30))
                .build();
    }

    // 1. Eventos de Pago
    public void handlePaymentCompleted(edu.dosw.rideci.infrastructure.messaging.events.payment.PaymentCompletedEvent event) {
        log.info("📩 Procesando PaymentCompletedEvent: paymentId={}, userId={}",
                event.getPaymentId(), event.getUserId());

        String message = String.format("Pago completado exitosamente por $%.2f. ID: %s",
                event.getAmount(), event.getPaymentId());

        NotificationEvent notificationEvent = createNotificationEvent(
                "PAYMENT",
                NotificationType.PAYMENT_CONFIRMED,
                event.getUserId(),
                message,
                String.format("{\"paymentId\":\"%s\",\"amount\":%.2f,\"tripId\":\"%s\"}",
                        event.getPaymentId(), event.getAmount(), event.getTripId())
        );

        InAppNotification inAppNotification = createInAppNotification(notificationEvent);
        log.info("✅ Notificación creada: {}", inAppNotification.getDisplayMessage());
    }

    public void handlePaymentFailed(edu.dosw.rideci.infrastructure.messaging.events.payment.PaymentFailedEvent event) {
        log.info("📩 Procesando PaymentFailedEvent: paymentId={}, userId={}",
                event.getPaymentId(), event.getUserId());

        String message = String.format("El pago por $%.2f ha fallado. ID: %s",
                event.getAmount(), event.getPaymentId());

        NotificationEvent notificationEvent = createNotificationEvent(
                "PAYMENT",
                NotificationType.PAYMENT_FAILED,
                event.getUserId(),
                message,
                String.format("{\"paymentId\":\"%s\",\"amount\":%.2f,\"tripId\":\"%s\"}",
                        event.getPaymentId(), event.getAmount(), event.getTripId())
        );

        InAppNotification inAppNotification = createInAppNotification(notificationEvent);
        inAppNotification.setPriority("HIGH");
        log.info("⚠️ Notificación de pago fallido creada: {}", inAppNotification.getDisplayMessage());
    }

    public void handlePaymentRefunded(edu.dosw.rideci.infrastructure.messaging.events.payment.PaymentRefundedEvent event) {
        log.info("📩 Procesando PaymentRefundedEvent: refundId={}, userId={}",
                event.getRefundId(), event.getUserId());

        String message = String.format("Reembolso procesado por $%.2f. ID Reembolso: %s",
                event.getAmount(), event.getRefundId());

        NotificationEvent notificationEvent = createNotificationEvent(
                "PAYMENT",
                NotificationType.PAYMENT_CONFIRMED,
                event.getUserId(),
                message,
                String.format("{\"refundId\":\"%s\",\"originalPaymentId\":\"%s\",\"amount\":%.2f}",
                        event.getRefundId(), event.getOriginalPaymentId(), event.getAmount())
        );

        InAppNotification inAppNotification = createInAppNotification(notificationEvent);
        log.info("💰 Notificación de reembolso creada: {}", inAppNotification.getDisplayMessage());
    }

    // 2. Eventos de Reportes/Seguridad
    public void handleReportCreated(edu.dosw.rideci.infrastructure.messaging.events.communication_security.ReportCreatedEvent event) {
        log.info("📩 Procesando ReportCreatedEvent: reportId={}, userId={}, severity={}",
                event.getReportId(), event.getUserId(), event.getSeverity());

        String message = String.format("Reporte %s creado: %s",
                event.getReportType(), event.getTitle());

        NotificationType notificationType;
        String priority = "NORMAL";

        if ("CRITICAL".equals(event.getSeverity()) || Boolean.TRUE.equals(event.getRequiresImmediateAction())) {
            notificationType = NotificationType.SECURITY_INCIDENT;
            priority = "HIGH";
        } else if ("EMERGENCY".equals(event.getReportType())) {
            notificationType = NotificationType.EMERGENCY_BUTTON_PRESSED;
            priority = "HIGH";
        } else {
            notificationType = NotificationType.SECURITY_REPORT_CREATED;
        }

        NotificationEvent notificationEvent = createNotificationEvent(
                "SECURITY",
                notificationType,
                event.getUserId(),
                message,
                String.format("{\"reportId\":\"%s\",\"type\":\"%s\",\"severity\":\"%s\",\"priority\":\"%s\"}",
                        event.getReportId(), event.getReportType(), event.getSeverity(), event.getPriority())
        );

        InAppNotification inAppNotification = createInAppNotification(notificationEvent);
        inAppNotification.setPriority(priority);
        inAppNotification.setTitle("Reporte de Seguridad");

        log.info("🛡️ Notificación de reporte creada: {}", inAppNotification.getDisplayMessage());

        // Notificar a administradores si es crítico
        if ("CRITICAL".equals(event.getSeverity()) || "EMERGENCY".equals(event.getPriority())) {
            log.warn("🚨 Notificando a administradores sobre reporte crítico: {}", event.getReportId());
        }
    }

    // 3. Eventos de Viajes
    public void handleTravelCreated(edu.dosw.rideci.infrastructure.messaging.events.travel.TravelCreatedEvent event) {
        log.info("📩 Procesando TravelCreatedEvent: travelId={}, driverId={}",
                event.getTravelId(), event.getDriverId());

        String message = String.format("Viaje creado exitosamente de %s a %s",
                event.getOrigin().getAddress(), event.getDestination().getAddress());

        NotificationEvent notificationEvent = createNotificationEvent(
                "TRAVEL",
                NotificationType.TRIP_CREATED,
                event.getDriverId(),
                message,
                String.format("{\"travelId\":\"%s\",\"origin\":\"%s\",\"destination\":\"%s\"}",
                        event.getTravelId(), event.getOrigin().getAddress(), event.getDestination().getAddress())
        );

        InAppNotification inAppNotification = createInAppNotification(notificationEvent);
        inAppNotification.setTitle("Viaje Creado");
        log.info("🚗 Notificación de viaje creado: {}", inAppNotification.getDisplayMessage());
    }

    public void handleTravelUpdated(edu.dosw.rideci.infrastructure.messaging.events.travel.TravelUpdatedEvent event) {
        log.info("📩 Procesando TravelUpdatedEvent: travelId={}, driverId={}, changes={}",
                event.getTravelId(), event.getDriverId(), event.getChanges());

        String message = String.format("Viaje actualizado: %s", event.getUpdateReason());

        NotificationEvent notificationEvent = createNotificationEvent(
                "TRAVEL",
                NotificationType.TRIP_UPDATED,
                event.getDriverId(),
                message,
                String.format("{\"travelId\":\"%s\",\"changes\":\"%s\",\"reason\":\"%s\"}",
                        event.getTravelId(), event.getChanges(), event.getUpdateReason())
        );

        InAppNotification inAppNotification = createInAppNotification(notificationEvent);
        inAppNotification.setTitle("Viaje Actualizado");
        log.info("✏️ Notificación de viaje actualizado: {}", inAppNotification.getDisplayMessage());
    }

    public void handleTravelCancelled(edu.dosw.rideci.infrastructure.messaging.events.travel.TravelCancelledEvent event) {
        log.info("📩 Procesando TravelCancelledEvent: travelId={}, driverId={}, reason={}",
                event.getTravelId(), event.getDriverId(), event.getCancellationReason());

        String message = String.format("Viaje cancelado: %s", event.getCancellationReason());

        NotificationEvent notificationEvent = createNotificationEvent(
                "TRAVEL",
                NotificationType.TRIP_CANCELLED,
                event.getDriverId(),
                message,
                String.format("{\"travelId\":\"%s\",\"reason\":\"%s\",\"affectedPassengers\":%d}",
                        event.getTravelId(), event.getCancellationReason(), event.getAffectedPassengers())
        );

        InAppNotification inAppNotification = createInAppNotification(notificationEvent);
        inAppNotification.setPriority("HIGH");
        inAppNotification.setTitle("Viaje Cancelado");
        log.info("❌ Notificación de viaje cancelado: {}", inAppNotification.getDisplayMessage());
    }

    public void handleTravelCompleted(edu.dosw.rideci.infrastructure.messaging.events.travel.TravelCompletedEvent event) {
        log.info("📩 Procesando TravelCompletedEvent: travelId={}, driverId={}, passengers={}",
                event.getTravelId(), event.getDriverId(), event.getPassengerIds().size());

        String message = "Viaje completado exitosamente";

        NotificationEvent notificationEvent = createNotificationEvent(
                "TRAVEL",
                NotificationType.TRIP_COMPLETED,
                event.getDriverId(),
                message,
                String.format("{\"travelId\":\"%s\",\"passengerCount\":%d,\"totalAmount\":%.2f}",
                        event.getTravelId(), event.getPassengerIds().size(), event.getTotalAmount())
        );

        InAppNotification inAppNotification = createInAppNotification(notificationEvent);
        inAppNotification.setTitle("Viaje Completado");
        log.info("✅ Notificación de viaje completado: {}", inAppNotification.getDisplayMessage());

        // Si ratingEnabled es true, crear notificación para calificación
        if (Boolean.TRUE.equals(event.getRatingEnabled())) {
            for (String passengerId : event.getPassengerIds()) {
                NotificationEvent ratingEvent = createNotificationEvent(
                        "TRAVEL",
                        NotificationType.PENDING_RATING_REMINDER,
                        passengerId,
                        "Por favor califica tu experiencia en el viaje",
                        String.format("{\"travelId\":\"%s\",\"driverId\":\"%s\"}",
                                event.getTravelId(), event.getDriverId())
                );

                InAppNotification ratingNotification = createInAppNotification(ratingEvent);
                ratingNotification.setTitle("Califica tu Viaje");
                ratingNotification.setExpiresAt(OffsetDateTime.now().plusDays(7));
                log.info("⭐ Notificación de calificación creada para pasajero: {}", passengerId);
            }
        }
    }

    // 4. Eventos de Reservas
    public void handleBookingCreated(edu.dosw.rideci.infrastructure.messaging.events.booking.BookingCreatedEvent event) {
        log.info("📩 Procesando BookingCreatedEvent: bookingId={}, travelId={}, passengerId={}",
                event.getBookingId(), event.getTravelId(), event.getPassengerId());

        String message = String.format("Reserva confirmada para viaje %s", event.getTravelId());

        NotificationEvent notificationEvent = createNotificationEvent(
                "BOOKING",
                NotificationType.RESERVATION_ACCEPTED,
                event.getPassengerId().toString(),
                message,
                String.format("{\"bookingId\":\"%s\",\"travelId\":\"%s\",\"seats\":%d}",
                        event.getBookingId(), event.getTravelId(), event.getReservedSeats())
        );

        InAppNotification inAppNotification = createInAppNotification(notificationEvent);
        inAppNotification.setTitle("Reserva Confirmada");
        log.info("🎫 Notificación de reserva creada: {}", inAppNotification.getDisplayMessage());
    }

    // 5. Eventos de Usuario/Autenticación
    public void handleUserEvent(edu.dosw.rideci.infrastructure.messaging.events.authentication.UserEvent event) {
        log.info("📩 Procesando UserEvent: userId={}, eventType={}, email={}",
                event.getUserId(), event.getEventType(), event.getEmail());

        NotificationType notificationType;
        String message;

        switch (event.getEventType()) {
            case "CREATED":
                notificationType = NotificationType.USER_REGISTERED;
                message = String.format("¡Bienvenido %s! Tu cuenta ha sido creada exitosamente.", event.getName());
                break;
            case "UPDATED":
                notificationType = NotificationType.NOTIFICATION_CREATED;
                message = "Tu perfil ha sido actualizado exitosamente.";
                break;
            case "DELETED":
                notificationType = NotificationType.NOTIFICATION_CREATED;
                message = "Tu cuenta ha sido eliminada del sistema.";
                break;
            default:
                notificationType = NotificationType.NOTIFICATION_CREATED;
                message = "Evento de usuario procesado.";
        }

        NotificationEvent notificationEvent = createNotificationEvent(
                "AUTHENTICATION",
                notificationType,
                event.getUserId(),
                message,
                String.format("{\"name\":\"%s\",\"email\":\"%s\",\"role\":\"%s\"}",
                        event.getName(), event.getEmail(), event.getRole())
        );

        InAppNotification inAppNotification = createInAppNotification(notificationEvent);
        inAppNotification.setTitle("Actualización de Cuenta");
        log.info("👤 Notificación de usuario creada: {}", inAppNotification.getDisplayMessage());
    }

    public void handlePasswordReset(edu.dosw.rideci.infrastructure.messaging.events.authentication.PasswordResetEvent event) {
        log.info("📩 Procesando PasswordResetEvent: userId={}, email={}",
                event.getUserId(), event.getEmail());

        String message = "Solicitud de recuperación de contraseña procesada";

        NotificationEvent notificationEvent = createNotificationEvent(
                "AUTHENTICATION",
                NotificationType.PASSWORD_RECOVERY,
                event.getUserId(),
                message,
                String.format("{\"email\":\"%s\",\"expiryMinutes\":%d}",
                        event.getEmail(), event.getExpiryMinutes())
        );

        InAppNotification inAppNotification = createInAppNotification(notificationEvent);
        inAppNotification.setTitle("Recuperación de Contraseña");
        inAppNotification.setPriority("HIGH");
        inAppNotification.setExpiresAt(OffsetDateTime.now().plusMinutes(event.getExpiryMinutes()));
        log.info("🔐 Notificación de recuperación de contraseña creada: {}", inAppNotification.getDisplayMessage());
    }
}