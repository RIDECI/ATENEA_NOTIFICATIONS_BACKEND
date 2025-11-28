package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Modelo de dominio para notificaciones in-app en RideECI.
 * Representa los mensajes mostrados al usuario dentro de la aplicación,
 * incluyendo su estado, prioridad y metadatos de auditoría.
 *
 * Esta entidad se utiliza en los casos de uso de notificaciones
 * y es persistida a través de los puertos de repositorio.
 *
 * @author RideECI
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InAppNotification {

    /** Identificador único de la notificación. */
    private UUID notificationId;

    /** Identificador del usuario destinatario de la notificación. */
    private UUID userId;

    /** Título breve de la notificación. */
    private String title;

    /** Mensaje detallado de la notificación. */
    private String message;

    /** Tipo de evento del dominio que originó la notificación. */
    private NotificationType eventType;

    /**
     * Prioridad de la notificación como texto.
     * Posibles valores: {@code "LOW"}, {@code "NORMAL"}, {@code "HIGH"}.
     */
    private String priority;

    /** Estado actual de la notificación (leída, no leída, archivada, expirada, etc.). */
    private NotificationStatus status;

    /** Fecha y hora en la que fue creada la notificación. */
    private OffsetDateTime createdAt;

    /** Fecha y hora en la que el usuario leyó la notificación. */
    private OffsetDateTime readAt;

    /** Fecha y hora en la que la notificación expira y deja de ser válida. */
    private OffsetDateTime expiresAt;

    /**
     * Marca la notificación como leída.
     * Actualiza el estado a {@link NotificationStatus#READ}
     * y asigna la fecha y hora actual a {@code readAt}.
     */
    public void markAsRead() {
        this.status = NotificationStatus.READ;
        this.readAt = OffsetDateTime.now();
    }

    /**
     * Indica si la notificación se encuentra expirada.
     * Una notificación se considera expirada si {@code expiresAt} no es nulo
     * y su valor es anterior al momento actual.
     *
     * @return {@code true} si la notificación está expirada, {@code false} en caso contrario.
     */
    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(OffsetDateTime.now());
    }

    /**
     * Construye un mensaje de visualización combinando título y contenido.
     * Útil para mostrar la notificación en listados o vistas resumidas.
     *
     * @return Cadena con formato {@code "<title> - <message>"}.
     */
    public String getDisplayMessage() {
        return title + " - " + message;
    }
}
