package edu.dosw.rideci.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.rideci.domain.model.Enum.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Evento de dominio utilizado para disparar notificaciones en RideECI.
 * Representa un suceso relevante generado por algún módulo de la plataforma.
 *
 * Contiene información del tipo de evento, origen, usuario afectado,
 * mensaje, prioridad, marca de tiempo y un payload adicional.
 *
 * @author RideECI
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {

    /** Identificador único del evento. */
    private String eventId;

    /** Tipo de evento que ocurrió en el dominio. */
    private EventType eventType;

    /** Nombre o identificador del módulo origen del evento. */
    private String sourceModule;

    /** Identificador del usuario asociado al evento (si aplica). */
    private String userId;

    /** Mensaje principal asociado al evento. */
    private String message;

    /** Prioridad numérica del evento (por ejemplo, 1 = alta, 5 = baja). */
    private int priority;

    /** Momento en el que ocurrió el evento. */
    private Instant timestamp;

    /** Payload adicional en formato texto (por ejemplo, JSON embebido). */
    private String payload;

    /** Mapper estático para serializar el evento a JSON. */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Serializa el evento a una representación JSON.
     *
     * En caso de fallo al serializar, devuelve un JSON mínimo con
     * {@code eventId} y {@code eventType} (o {@code UNKNOWN} si es nulo).
     *
     * @return Cadena JSON que representa este {@link NotificationEvent}.
     */
    public String toJSON() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"eventId\":\"" + eventId + "\",\"eventType\":\"" +
                    (eventType != null ? eventType.name() : "UNKNOWN") + "\"}";
        }
    }

}
