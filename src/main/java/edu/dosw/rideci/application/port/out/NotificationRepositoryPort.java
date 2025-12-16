package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.InAppNotification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para el acceso a datos de notificaciones in-app.
 * Define las operaciones de persistencia y consulta sobre el modelo
 * {@link InAppNotification}.
 *
 * @author RideECI
 * @version 1.0
 */
public interface NotificationRepositoryPort {

    /**
     * Guarda una notificación in-app en el repositorio.
     *
     * @param notification Notificación a persistir.
     * @return Notificación almacenada, posiblemente con datos generados
     *         por el repositorio (por ejemplo, identificador o marcas de tiempo).
     */
    InAppNotification save(InAppNotification notification);

    /**
     * Busca una notificación por su identificador único.
     *
     * @param id Identificador de la notificación.
     * @return {@link Optional} que puede contener la notificación si existe,
     *         o vacío si no se encuentra.
     */
    Optional<InAppNotification> findById(UUID id);

    /**
     * Obtiene la lista de notificaciones de un usuario ordenadas por fecha
     * de creación descendente (de la más reciente a la más antigua).
     *
     * @param userId Identificador del usuario.
     * @return Lista de notificaciones asociadas al usuario. Puede ser una lista vacía
     *         si el usuario no tiene notificaciones registradas.
     */
    List<InAppNotification> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
