package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Enum.NotificationType;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface SendEmailNotificationUseCase {

    void send(SendEmailNotificationCommand command);

    @Builder
    record SendEmailNotificationCommand(
            NotificationType type,
            UUID userId,
            String emailOverride,
            String tripId,
            String driverId,
            String paymentId,
            String reason,
            String extraInfo,
            OffsetDateTime scheduledAt
    ) {}
}
