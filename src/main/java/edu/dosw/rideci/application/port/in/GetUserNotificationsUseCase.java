package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.InAppNotification;

import java.util.List;
import java.util.UUID;

/**
 * Caso de uso para la consulta de notificaciones in-app de un usuario.
 * Permite obtener las notificaciones asociadas a un identificador de usuario.
 *
 * @author RideECI
 * @version 1.0
 */
public interface GetUserNotificationsUseCase {

    /**
     * Obtiene todas las notificaciones in-app asociadas a un usuario.
     *
     * @param userId Identificador único del usuario.
     * @return Lista de notificaciones del usuario. Puede ser una lista vacía
     *         si el usuario no tiene notificaciones registradas.
     */
    List<InAppNotification> getNotificationsByUserId(UUID userId);
}
