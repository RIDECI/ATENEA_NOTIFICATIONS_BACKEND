package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.NotificationEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Bus de eventos de dominio para notificaciones en RideECI.
 *
 * Administra la publicación y distribución de {@link NotificationEvent}
 * hacia los suscriptores registrados ({@link NotificationSubscriber}),
 * utilizando una cola interna y un hilo de procesamiento en segundo plano.
 *
 * @author RideECI
 * @version 1.0
 */
@Slf4j
@Component
public class EventBus {

    /** Suscriptores registrados por tipo de evento. */
    private final Map<EventType, List<NotificationSubscriber>> subscribers = new ConcurrentHashMap<>();

    /** Cola de eventos pendientes por procesar. */
    private final BlockingQueue<NotificationEvent> eventQueue = new LinkedBlockingQueue<>();

    /** Bandera para controlar el ciclo de procesamiento. */
    private volatile boolean isRunning = false;

    /**
     * Publica un evento en el bus.
     * El evento se encola para ser procesado de forma asíncrona
     * por el hilo de trabajo.
     *
     * @param event Evento de notificación a publicar. Si es {@code null}, se ignora.
     */
    public void publish(NotificationEvent event) {
        if (event == null) {
            return;
        }
        eventQueue.offer(event);
    }

    /**
     * Registra un suscriptor para un tipo de evento específico.
     *
     * @param eventType  Tipo de evento al que se desea suscribir.
     * @param subscriber Suscriptor que será notificado cuando se publique un evento de ese tipo.
     */
    public void subscribe(EventType eventType, NotificationSubscriber subscriber) {
        subscribers
                .computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(subscriber);
    }

    /**
     * Elimina un suscriptor para un tipo de evento específico.
     * Si ya no quedan suscriptores para ese tipo, lo remueve del mapa.
     *
     * @param eventType  Tipo de evento del que se desea desuscribir.
     * @param subscriber Suscriptor a eliminar.
     */
    public void unsubscribe(EventType eventType, NotificationSubscriber subscriber) {
        List<NotificationSubscriber> list = subscribers.get(eventType);
        if (list != null) {
            list.remove(subscriber);
            if (list.isEmpty()) {
                subscribers.remove(eventType);
            }
        }
    }

    /**
     * Obtiene el tamaño actual de la cola de eventos pendientes.
     *
     * @return Número de eventos encolados.
     */
    public int getQueueSize() {
        return eventQueue.size();
    }

    /**
     * Inicializa el bus de eventos al construir el componente.
     * Arranca un hilo daemon que ejecuta el ciclo de procesamiento
     * de eventos de forma continua.
     */
    @PostConstruct
    public void start() {
        isRunning = true;
        Thread worker = new Thread(this::processLoop, "notification-event-bus");
        worker.setDaemon(true);
        worker.start();
        log.info("EventBus started");
    }

    /**
     * Detiene el bus de eventos antes de destruir el componente.
     * Marca el ciclo de procesamiento como detenido.
     */
    @PreDestroy
    public void stop() {
        isRunning = false;
        log.info("EventBus stopped");
    }

    /**
     * Bucle principal de procesamiento de eventos.
     * Extrae eventos de la cola y los distribuye a todos los
     * suscriptores registrados para el tipo de evento correspondiente.
     *
     * En caso de error al notificar a un suscriptor, se registra en el log
     * pero no se detiene el procesamiento de los demás.
     */
    private void processLoop() {
        while (isRunning) {
            try {
                NotificationEvent event = eventQueue.take();
                List<NotificationSubscriber> subs =
                        subscribers.getOrDefault(event.getEventType(), List.of());
                for (NotificationSubscriber s : subs) {
                    try {
                        s.handleEvent(event);
                    } catch (Exception e) {
                        log.error("Error handling event {} by {}", event.getEventId(), s.getName(), e);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
