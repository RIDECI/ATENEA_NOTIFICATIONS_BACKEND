package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.Enum.NotificationChannel;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de respuesta para notificaciones.
 * Representa la información que se expone al cliente sobre una
 * notificación in-app u otros canales configurados.
 *
 * Incluye datos de contenido, canal, estado y metadatos de auditoría.
 *
 * @author RideECI
 * @version 1.0
 */
@Getter
@Setter
@Builder
public class NotificationResponse {

    /** Identificador único de la notificación. */
    private UUID notificationId;

    /** Identificador del usuario destinatario de la notificación. */
    private UUID userId;

    /** Tipo de evento asociado a la notificación. */
    private EventType eventType;

    /** Canal por el que se entrega la notificación. */
    private NotificationChannel channel;

    /** Título de la notificación. */
    private String title;

    /** Mensaje o contenido principal de la notificación. */
    private String message;

    /**
     * Prioridad de la notificación (por ejemplo "LOW", "NORMAL", "HIGH").
     */
    private String priority;

    /**
     * Metadata adicional en formato JSON asociada a la notificación.
     */
    private String metadataJson;

    /** Estado actual de la notificación (no leída, leída, archivada, expirada, etc.). */
    private NotificationStatus status;

    /** Fecha y hora de creación de la notificación. */
    private OffsetDateTime createdAt;

    /** Fecha y hora en la que la notificación fue leída. */
    private OffsetDateTime readAt;

    /** Fecha y hora en la que la notificación expira. */
    private OffsetDateTime expiresAt;
}
