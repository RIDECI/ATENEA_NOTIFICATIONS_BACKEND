package edu.dosw.rideci.domain.model.Enum;

/**
 * Estados posibles de una notificación dentro del sistema RideECI.
 * Permite determinar si una notificación ha sido leída, archivada
 * o si ha expirado según las reglas del dominio.
 *
 * Estos estados ayudan al usuario y al sistema a gestionar el ciclo de vida
 * de cada notificación de manera ordenada.
 *
 * @author RideECI
 * @version 1.0
 */
public enum NotificationStatus {

    /** Notificación no leída por el usuario. */
    UNREAD,

    /** Notificación que ya fue abierta o leída. */
    READ,

    /** Notificación archivada por el usuario o el sistema. */
    ARCHIVED,

    /** Notificación marcada como expirada según reglas de validez. */
    EXPIRED
}
