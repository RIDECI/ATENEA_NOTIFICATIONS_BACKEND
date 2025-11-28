package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.NotificationEvent;

import java.util.List;

/**
 * Contrato para los suscriptores de eventos de notificación.
 * Define las operaciones necesarias para que un componente
 * pueda reaccionar a eventos publicados en el {@link EventBus}.
 *
 * Cada implementación indica a qué tipos de eventos se suscribe
 * y proporciona un nombre identificador.
 *
 * @author RideECI
 * @version 1.0
 */
public interface NotificationSubscriber {

    /**
     * Maneja un evento de notificación recibido desde el {@link EventBus}.
     *
     * @param event Evento de notificación a procesar.
     */
    void handleEvent(NotificationEvent event);

    /**
     * Devuelve la lista de tipos de eventos a los que este suscriptor
     * desea estar registrado.
     *
     * @return Lista de {@link EventType} soportados por el suscriptor.
     */
    List<EventType> getSubscribedEvents();

    /**
     * Devuelve el nombre identificador del suscriptor.
     * Útil para propósitos de logging y monitoreo.
     *
     * @return Nombre del suscriptor.
     */
    String getName();
}
