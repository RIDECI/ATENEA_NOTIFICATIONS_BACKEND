package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.NotificationEvent;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
public class EventBus {

    private final Map<EventType, List<NotificationSubscriber>> subscribers = new HashMap<>();
    private final BlockingQueue<NotificationEvent> eventQueue = new LinkedBlockingQueue<>();
    private volatile boolean isRunning = false;

    public void publish(NotificationEvent event) {
        eventQueue.offer(event);
    }

    public void subscribe(EventType eventType, NotificationSubscriber subscriber) {
        subscribers
                .computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(subscriber);
    }

    public void unsubscribe(EventType eventType, NotificationSubscriber subscriber) {
        subscribers.getOrDefault(eventType, List.of()).remove(subscriber);
    }

    public int getQueueSize() {
        return eventQueue.size();
    }

    @PostConstruct
    public void start() {
        isRunning = true;
        Thread worker = new Thread(this::processLoop, "notification-event-bus");
        worker.setDaemon(true);
        worker.start();
    }

    public void stop() {
        isRunning = false;
    }

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
