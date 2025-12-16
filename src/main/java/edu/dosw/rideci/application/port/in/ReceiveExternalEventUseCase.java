package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.NotificationEvent;

/**
 * Caso de uso para la recepción de eventos externos relacionados con notificaciones.
 * Permite ingresar al sistema eventos provenientes de otros servicios o módulos
 * para su posterior procesamiento.
 *
 * @author RideECI
 * @version 1.0
 */
public interface ReceiveExternalEventUseCase {

    /**
     * Recibe un evento externo y lo envía al flujo de procesamiento de notificaciones.
     *
     * @param event Evento externo que contiene la información necesaria
     *              para generar o actualizar notificaciones.
     */
    void receive(NotificationEvent event);
}
