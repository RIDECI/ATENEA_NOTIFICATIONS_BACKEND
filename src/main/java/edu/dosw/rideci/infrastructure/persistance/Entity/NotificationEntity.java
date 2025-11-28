package edu.dosw.rideci.infrastructure.persistance.Entity;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Entidad de persistencia para notificaciones.
 * Representa la estructura almacenada en la capa de datos para una notificación,
 * desacoplada del modelo de dominio {@code InAppNotification}.
 *
 * Contiene información de usuario, contenido, tipo de evento, prioridad,
 * estado y metadatos de auditoría (fechas de creación, lectura y expiración).
 *
 * @author RideECI
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {

    /** Identificador único de la notificación en la base de datos. */
    private UUID notificationId;

    /** Identificador del usuario destinatario de la notificación. */
    private UUID userId;

    /** Título de la notificación. */
    private String title;

    /** Mensaje o contenido principal de la notificación. */
    private String message;

    /** Tipo de evento asociado a la notificación. */
    private EventType eventType;

    /**
     * Prioridad serializada como texto para desacoplar de enums concretos.
     * Ejemplos: "LOW", "NORMAL", "HIGH".
     */
    private String priority;

    /** Estado actual de la notificación (no leída, leída, archivada, expirada, etc.). */
    private NotificationStatus status;

    /** Fecha y hora de creación de la notificación. */
    private OffsetDateTime createdAt;

    /** Fecha y hora en la que la notificación fue leída. */
    private OffsetDateTime readAt;

    /** Fecha y hora en la que la notificación expira. */
    private OffsetDateTime expiresAt;
}
