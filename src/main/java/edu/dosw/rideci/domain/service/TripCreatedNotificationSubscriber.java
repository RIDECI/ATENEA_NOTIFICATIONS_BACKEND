package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.NotificationEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Suscriptor de dominio para eventos {@link NotificationType#TRIP_CREATED}.
 *
 * Se registra en el {@link EventBus} al inicializarse y procesa
 * los eventos de viaje creado, permitiendo ejecutar lógica específica
 * asociada a este tipo de evento (por ahora, solo logging).
 *
 * @author RideECI
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TripCreatedNotificationSubscriber implements NotificationSubscriber {

    private final EventBus eventBus;

    /**
     * Registra este suscriptor en el {@link EventBus} para el evento
     * {@link NotificationType#TRIP_CREATED} al iniciar el componente.
     */
    @PostConstruct
    public void init() {
        eventBus.subscribe(NotificationType.TRIP_CREATED, this);
        log.info("Subscriber registrado para TRIP_CREATED");
    }

    /**
     * Desregistra este suscriptor del {@link EventBus} antes de destruir
     * el componente, evitando recibir más eventos.
     */
    @PreDestroy
    public void destroy() {
        eventBus.unsubscribe(NotificationType.TRIP_CREATED, this);
        log.info("Subscriber removido para TRIP_CREATED");
    }

    /**
     * Lógica específica cuando llega un evento {@link NotificationType#TRIP_CREATED}.
     * Actualmente solo registra en el log el procesamiento del evento.
     *
     * @param event Evento de viaje creado.
     */
    public void onEvent(NotificationEvent event) {
        log.info("Procesando evento TRIP_CREATED para userId={}", event.getUserId());
    }

    /**
     * Maneja un evento recibido desde el {@link EventBus}.
     * En este suscriptor, delega en {@link #onEvent(NotificationEvent)}.
     *
     * @param event Evento de notificación a procesar.
     */
    @Override
    public void handleEvent(NotificationEvent event) {
        if (event == null) {
            return;
        }
        onEvent(event);
    }

    /**
     * Devuelve la lista de tipos de evento a los que este suscriptor
     * está interesado en reaccionar.
     *
     * @return Lista con {@link NotificationType#TRIP_CREATED}.
     */
    @Override
    public List<NotificationType> getSubscribedEvents() {
        return List.of(NotificationType.TRIP_CREATED);
    }

    /**
     * Devuelve el nombre identificador del suscriptor.
     *
     * @return Nombre del suscriptor.
     */
    @Override
    public String getName() {
        return "trip-created-notification-subscriber";
    }
}
