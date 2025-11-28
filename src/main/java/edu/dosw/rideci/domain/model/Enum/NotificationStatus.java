package edu.dosw.rideci.domain.model.Enum;

/**
 * Estados posibles de una notificación dentro del sistema RideECI.
 *
 * Este enum modela todo el ciclo de vida de una notificación:
 * - Creación y programación
 * - Intentos de envío y resultado (éxito / fallo / cancelación)
 * - Interacción del usuario (leída / no leída)
 * - Organización (archivada) y expiración
 *
 * Estos estados permiten al módulo de notificaciones orquestar
 * reintentos, métricas y reglas de negocio de forma consistente.
 *
 * @author RideECI
 * @version 1.1
 */
public enum NotificationStatus {

    /**
     * Notificación creada pero aún no enviada.
     * Útil para colas de envío y revisiones previas.
     */
    PENDING,

    /**
     * Notificación programada para enviarse en una fecha/hora futura.
     * Aplica para recordatorios de viajes próximos u otras alertas planificadas.
     */
    SCHEDULED,

    /**
     * El sistema está intentando entregar la notificación
     * al canal correspondiente (correo, push, in-app, etc.).
     */
    SENDING,

    /**
     * La notificación fue enviada correctamente al canal definido.
     * No implica necesariamente que el usuario la haya leído.
     */
    SENT,

    /**
     * El intento de envío falló (por ejemplo, error SMTP, canal no disponible).
     * Este estado puede activar políticas de reintento.
     */
    FAILED,

    /**
     * La notificación fue cancelada explícitamente por el sistema
     * o por una regla de negocio (por ejemplo, el viaje fue eliminado).
     */
    CANCELLED,

    /**
     * Notificación entregada pero aún no leída por el usuario.
     * Se utiliza principalmente para in-app y push.
     */
    UNREAD,

    /**
     * Notificación que ya fue abierta o marcada como leída por el usuario.
     */
    READ,

    /**
     * Notificación archivada por el usuario o por el sistema
     * para mantener la bandeja principal limpia, pero sin perder
     * el historial.
     */
    ARCHIVED,

    /**
     * Notificación que ya no es relevante por haber superado
     * su periodo de validez (por ejemplo, después de la fecha del viaje).
     */
    EXPIRED
}
