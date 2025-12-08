package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.Enum.BroadcastType;

import java.util.List;

/**
 * Caso de uso para crear un broadcast de emergencia.
 * Este caso de uso genera múltiples notificaciones para los usuarios objetivo.
 *
 * Puede ser utilizado por administradores en situaciones extraordinarias
 * (incidentes de seguridad, emergencias, fallas críticas del sistema, etc.).
 */
public interface CreateEmergencyBroadcastUseCase {

    /**
     * Ejecuta el broadcast de emergencia.
     *
     * @param command Datos necesarios para crear las notificaciones.
     * @return Lista de notificaciones creadas.
     */
    List<InAppNotification> createEmergencyBroadcast(CreateEmergencyBroadcastCommand command);

    /**
     * Comando que encapsula los datos del broadcast.
     */
    record CreateEmergencyBroadcastCommand(
            String emergencyMessage,
            BroadcastType broadcastType,
            List<String> targetUserIds,
            String priorityLevel
    ) {}
}
