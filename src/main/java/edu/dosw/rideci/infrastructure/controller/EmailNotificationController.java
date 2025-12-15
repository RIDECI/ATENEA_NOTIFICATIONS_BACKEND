package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.SendEmailNotificationUseCase;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.infrastructure.controller.dto.Request.EmailNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailNotificationController {

    private final SendEmailNotificationUseCase sendEmailUseCase;

    @PostMapping("/password-recovery")
    public ResponseEntity<Void> sendPasswordRecovery(@RequestBody EmailNotificationRequest request) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.PASSWORD_RECOVERY)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .reason(request.reason())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/registration-verification")
    public ResponseEntity<Void> sendRegistrationVerification(@RequestBody EmailNotificationRequest request) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.USER_REGISTERED)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .reason(request.reason())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/driver-verification-result")
    public ResponseEntity<Void> sendDriverVerificationResult(@RequestBody EmailNotificationRequest request) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.DRIVER_VALIDATED)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .driverId(request.driverId())
                        .reason(request.reason())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/trip-booking-confirmation")
    public ResponseEntity<Void> sendTripBookingConfirmation(@RequestBody EmailNotificationRequest request) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.RESERVATION_ACCEPTED)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .tripId(request.tripId())
                        .reason(request.reason())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }
    @PostMapping("/travel/created")
    public ResponseEntity<Void> sendTravelCreatedNotification(
            @RequestBody EmailNotificationRequest request
    ) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.TRIP_CREATED)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/travel/updated")
    public ResponseEntity<Void> sendTravelUpdatedNotification(
            @RequestBody EmailNotificationRequest request
    ) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.TRIP_UPDATED)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .reason(request.reason())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/trip-update")
    public ResponseEntity<Void> sendTripUpdate(@RequestBody EmailNotificationRequest request) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.TRIP_UPDATED)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .tripId(request.tripId())
                        .reason(request.reason())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/trip-reminder")
    public ResponseEntity<Void> sendTripReminder(@RequestBody EmailNotificationRequest request) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.UPCOMING_TRIP_REMINDER)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .tripId(request.tripId())
                        .reason(request.reason())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }


    @PostMapping("/travel/completed")
    public ResponseEntity<Void> sendTravelCompletedNotification(
            @RequestBody EmailNotificationRequest request
    ) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.TRIP_COMPLETED)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/travel/cancelled")
    public ResponseEntity<Void> sendTravelCancelledNotification(
            @RequestBody EmailNotificationRequest request
    ) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.TRIP_CANCELLED)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .reason(request.reason())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/payment-confirmation")
    public ResponseEntity<Void> sendPaymentConfirmation(@RequestBody EmailNotificationRequest request) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.PAYMENT_CONFIRMED)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .tripId(request.tripId())
                        .paymentId(request.paymentId())
                        .reason(request.reason())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }


    @PostMapping("/account-suspension")
    public ResponseEntity<Void> sendAccountSuspension(@RequestBody EmailNotificationRequest request) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.ACCOUNT_SUSPENDED)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .reason(request.reason())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/emergency-alert")
    public ResponseEntity<Void> sendEmergencyAlert(@RequestBody EmailNotificationRequest request) {
        sendEmailUseCase.send(
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .type(NotificationType.EMERGENCY_ALERT)
                        .userId(request.userId())
                        .emailOverride(request.email())
                        .tripId(request.tripId())
                        .reason(request.reason())
                        .extraInfo(request.extraInfo())
                        .scheduledAt(request.scheduledAt())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }
}
