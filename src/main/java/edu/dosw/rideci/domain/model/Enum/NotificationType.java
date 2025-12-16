package edu.dosw.rideci.domain.model.Enum;

/**
 * Tipos de eventos de dominio que pueden generar notificaciones
 * u otras acciones dentro de la plataforma RideECI.
 *
 * Representa los diferentes hitos o sucesos relevantes en el ciclo
 * de vida de usuarios, conductores, viajes, pagos y seguridad.
 *
 * Esta enumeración está alineada con los RF-NOT-001 al RF-NOT-012
 * del módulo de notificaciones.
 *
 * @author RideECI
 * @version 1.1
 */
public enum NotificationType {

    /** Usuario registrado en la plataforma. */
    USER_REGISTERED,

    /** Actualización de la información del usuario. */
    USER_UPDATED,

    /** Conductor validado/aprobado en el sistema. (Módulo 7) */
    DRIVER_VALIDATED,

    /** Cuenta de usuario o conductor suspendida. (RF-NOT-010) */
    ACCOUNT_SUSPENDED,

    /** Cuenta de usuario o conductor reactivada. (RF-NOT-010) */
    ACCOUNT_REACTIVATED,

    /** Nuevo distintivo asignado a un usuario o conductor. (Módulo 6) */
    NEW_DISTINTIVE,

    /** Nueva reserva aceptada por el conductor. (RF-NOT-002) */
    RESERVATION_ACCEPTED,

    /** Reserva rechazada por el conductor o el sistema. (RF-NOT-002) */
    RESERVATION_REJECTED,

    /** Nuevo viaje creado. (Módulo 2) */
    TRIP_CREATED,

    /** Actualización de la información de un viaje (horario, ruta, etc.). (RF-NOT-001) */
    TRIP_UPDATED,

    /** Viaje cancelado. (RF-NOT-001) */
    TRIP_CANCELLED,

    /** Viaje iniciado. (RF-NOT-003) */
    TRIP_STARTED,

    /** Viaje completado. (RF-NOT-003) */
    TRIP_COMPLETED,

    /** Desviación de ruta detectada respecto a la estimada. (RF-NOT-004, Módulo 4) */
    ROUTE_DEVIATION_DETECTED,

    /** Recordatorio de un viaje próximo. (RF-NOT-006) */
    UPCOMING_TRIP_REMINDER,

    /** Recordatorio de calificación pendiente después del viaje. (RF-NOT-007) */
    PENDING_RATING_REMINDER,

    /** Pago confirmado. (Módulo 3) */
    PAYMENT_CONFIRMED,

    /** Pago fallido. (Módulo 3) */
    PAYMENT_FAILED,

    /** Recuperación de contraseña o cuenta solicitada por el usuario. (RF-NOT-008) */
    PASSWORD_RECOVERY,

    /** Alerta relacionada con la ubicación (por ejemplo, zonas de riesgo). */
    LOCATION_ALERT,

    /** Incidente de seguridad reportado por un usuario. (RF-NOT-009, Reportes) */
    SECURITY_INCIDENT,

    /** Nuevo reporte de seguridad creado que debe ser notificado a administradores. (RF-NOT-009) */
    SECURITY_REPORT_CREATED,

    /** Botón de emergencia activado por un usuario. (RF-NOT-012, Módulo 5) */
    EMERGENCY_BUTTON_PRESSED,

    /** Notificación de emergencia enviada a los usuarios/administradores. (RF-NOT-012) */
    EMERGENCY_ALERT,

    /** Calificación registrada por un usuario. */
    RATING_SUBMITTED,

    /** Notificación genérica creada en el sistema. */
    NOTIFICATION_CREATED,

    /** Notificación específicamente pensada para canal de correo electrónico. (RF-NOT-011) */
    GENERIC_EMAIL_NOTIFICATION
}
