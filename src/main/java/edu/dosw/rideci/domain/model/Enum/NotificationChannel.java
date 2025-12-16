package edu.dosw.rideci.domain.model.Enum;

/**
 * Canales disponibles para el envío de notificaciones dentro de RideECI.
 * Define los medios por los cuales un usuario puede recibir alertas o mensajes.
 *
 * Los canales permiten que el sistema soporte múltiples mecanismos
 * de entrega como correo electrónico, notificaciones push o notificaciones internas.
 *
 * @author RideECI
 * @version 1.0
 */
public enum NotificationChannel {

    /** Notificación enviada por correo electrónico. */
    EMAIL,

    /** Notificación enviada mediante push (dispositivos móviles u otros). */
    PUSH,

    /** Notificación interna mostrada dentro de la aplicación. */
    IN_APP
}
