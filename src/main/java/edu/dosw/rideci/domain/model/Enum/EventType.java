package edu.dosw.rideci.domain.model.Enum;

/**
 * Tipos de eventos de dominio que pueden generar notificaciones
 * u otras acciones dentro de la plataforma RideECI.
 *
 * Representa los diferentes hitos o sucesos relevantes en el ciclo
 * de vida de usuarios, conductores, viajes y pagos.
 *
 * @author RideECI
 * @version 1.0
 */
public enum EventType {

    /** Usuario registrado en la plataforma. */
    USER_REGISTERED,

    /** Conductor validado/aprobado en el sistema. */
    DRIVER_VALIDATED,

    /** Viaje cancelado. */
    TRIP_CANCELLED,

    /** Nuevo viaje creado. */
    TRIP_CREATED,

    /** Viaje completado. */
    TRIP_COMPLETED,

    /** Pago confirmado. */
    PAYMENT_CONFIRMED,

    /** Pago fallido. */
    PAYMENT_FAILED,

    /** Alerta relacionada con la ubicación (por ejemplo, zonas de riesgo). */
    LOCATION_ALERT,

    /** Incidente de seguridad reportado. */
    SECURITY_INCIDENT,

    /** Calificación registrada por un usuario. */
    RATING_SUBMITTED,

    /** Nuevo distintivo asignado a un usuario o conductor. */
    NEW_DISTINTIVE,

    /** Botón de emergencia activado. */
    EMERGY_BOTON,

    /** Actualización de la información de un viaje. */
    TRIP_UPDATED
}
