package edu.dosw.rideci.infrastructure.controller.dto.Request;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Request genérico para disparar correos de notificación.
 * Cada endpoint rellenará solo los campos que le apliquen.
 */
public record EmailNotificationRequest(
        UUID userId,
        String email,
        String tripId,
        String driverId,
        String paymentId,
        String reason,
        String extraInfo,
        OffsetDateTime scheduledAt
) {}
