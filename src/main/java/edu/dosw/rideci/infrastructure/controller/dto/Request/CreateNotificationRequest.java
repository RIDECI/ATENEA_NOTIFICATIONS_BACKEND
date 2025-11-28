package edu.dosw.rideci.infrastructure.controller.dto.Request;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.Enum.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * DTO de request para la creación de una notificación.
 * Representa los datos enviados desde el cliente para registrar
 * una nueva notificación en el sistema.
 *
 * Incluye información del usuario destino, tipo de evento,
 * canal de notificación y contenido del mensaje.
 *
 * @author RideECI
 * @version 1.0
 */
@Getter
@Setter
public class CreateNotificationRequest {

    /** Identificador del usuario destinatario de la notificación. */
    @NotNull
    private UUID userId;

    /** Tipo de evento asociado a la notificación. */
    @NotNull
    private EventType eventType;

    /** Canal por el cual se enviará la notificación (email, push, in-app, etc.). */
    @NotNull
    private NotificationChannel channel;

    /** Título de la notificación. */
    @NotBlank
    private String title;

    /** Contenido principal de la notificación. */
    @NotBlank
    private String message;

    /**
     * Prioridad de la notificación (por ejemplo "LOW", "NORMAL", "HIGH").
     * Campo opcional.
     */
    private String priority;

    /**
     * Información adicional en formato JSON (metadata de la notificación).
     * Campo opcional.
     */
    private String metadataJson;
}
