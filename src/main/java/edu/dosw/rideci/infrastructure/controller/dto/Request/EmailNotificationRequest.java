package edu.dosw.rideci.infrastructure.controller.dto.Request;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Request genérico para disparar correos de notificación.
 * Cada endpoint rellenará solo los campos que le apliquen.
 */
public record EmailNotificationRequest(
        UUID userId,          // id del usuario (cuando lo tengas)
        String email,         // opcional, si envías correo directo
        String tripId,        // para correos de viaje
        String driverId,      // verificación de conductor
        String paymentId,     // confirmación de pago
        String reason,        // motivo (suspensión, cancelación, rechazo, etc.)
        String extraInfo,     // texto adicional
        OffsetDateTime scheduledAt // para recordatorios, si aplica
) {}
