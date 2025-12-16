package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.ReceiveExternalEventUseCase;
import edu.dosw.rideci.domain.model.NotificationEvent;
import edu.dosw.rideci.domain.service.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación para la recepción de eventos externos de notificación.
 * Publica los eventos recibidos en el {@link EventBus} para que sean
 * procesados por los suscriptores correspondientes.
 *
 * @author RideECI
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ReceiveExternalEventService implements ReceiveExternalEventUseCase {

    private final EventBus eventBus;

    /**
     * Recibe un evento externo y lo publica en el bus de eventos.
     *
     * @param event Evento de notificación recibido desde un sistema externo
     *              o desde otro contexto de la aplicación.
     */
    @Override
    public void receive(NotificationEvent event) {
        eventBus.publish(event);
    }
}
