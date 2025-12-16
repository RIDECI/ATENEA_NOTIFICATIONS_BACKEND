package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.InAppNotification;

import java.util.UUID;

/**
 * Caso de uso para marcar una notificación in-app como leída.
 * Define el contrato que permite actualizar el estado de una notificación.
 *
 * @author RideECI
 * @version 1.0
 */
public interface MarkNotificationAsReadUseCase {

    /**
     * Marca una notificación como leída según su identificador.
     *
     * @param notificationId Identificador único de la notificación.
     * @return La notificación actualizada con estado de lectura.
     */
    InAppNotification markAsRead(UUID notificationId);
}
