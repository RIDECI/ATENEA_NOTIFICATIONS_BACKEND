package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.InAppNotification;

/**
 * Caso de uso para la creación de notificaciones in-app.
 * Define el contrato para registrar nuevas notificaciones dentro del sistema.
 *
 * @author RideECI
 * @version 1.0
 */
public interface CreateNotificationUseCase {

    /**
     * Crea una nueva notificación in-app.
     *
     * @param notification Notificación a crear.
     * @return Notificación creada, posiblemente con datos adicionales (ID, fechas, etc.).
     */
    InAppNotification createNotification(InAppNotification notification);
}
