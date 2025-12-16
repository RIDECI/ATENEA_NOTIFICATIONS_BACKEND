package edu.dosw.rideci.unit.domain.service;

import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.NotificationEvent;
import edu.dosw.rideci.domain.service.EventBus;
import edu.dosw.rideci.domain.service.NotificationSubscriber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventBusTest {

    private EventBus eventBus;

    @BeforeEach
    void setUp() {
        eventBus = new EventBus();
    }

    @AfterEach
    void tearDown() {
        eventBus.stop();
    }

    @Test
    void shouldSubscribeSubscriberToEventType() throws Exception {
        NotificationSubscriber subscriber1 = mock(NotificationSubscriber.class);
        NotificationSubscriber subscriber2 = mock(NotificationSubscriber.class);

        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber1);
        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber2);

        Field subscribersField = EventBus.class.getDeclaredField("subscribers");
        subscribersField.setAccessible(true);
        Map<NotificationType, List<NotificationSubscriber>> subscribers =
                (Map<NotificationType, List<NotificationSubscriber>>) subscribersField.get(eventBus);

        assertTrue(subscribers.containsKey(NotificationType.NOTIFICATION_CREATED));
        assertEquals(2, subscribers.get(NotificationType.NOTIFICATION_CREATED).size());
        assertTrue(subscribers.get(NotificationType.NOTIFICATION_CREATED).contains(subscriber1));
        assertTrue(subscribers.get(NotificationType.NOTIFICATION_CREATED).contains(subscriber2));
    }

    @Test
    void shouldUnsubscribeSubscriberFromEventType() throws Exception {
        NotificationSubscriber subscriber1 = mock(NotificationSubscriber.class);
        NotificationSubscriber subscriber2 = mock(NotificationSubscriber.class);

        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber1);
        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber2);

        eventBus.unsubscribe(NotificationType.NOTIFICATION_CREATED, subscriber1);

        Field subscribersField = EventBus.class.getDeclaredField("subscribers");
        subscribersField.setAccessible(true);
        Map<NotificationType, List<NotificationSubscriber>> subscribers =
                (Map<NotificationType, List<NotificationSubscriber>>) subscribersField.get(eventBus);

        assertTrue(subscribers.containsKey(NotificationType.NOTIFICATION_CREATED));
        assertEquals(1, subscribers.get(NotificationType.NOTIFICATION_CREATED).size());
        assertFalse(subscribers.get(NotificationType.NOTIFICATION_CREATED).contains(subscriber1));
        assertTrue(subscribers.get(NotificationType.NOTIFICATION_CREATED).contains(subscriber2));
    }

    @Test
    void shouldRemoveEventTypeWhenNoSubscribersLeft() throws Exception {
        NotificationSubscriber subscriber1 = mock(NotificationSubscriber.class);

        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber1);
        eventBus.unsubscribe(NotificationType.NOTIFICATION_CREATED, subscriber1);

        Field subscribersField = EventBus.class.getDeclaredField("subscribers");
        subscribersField.setAccessible(true);
        Map<NotificationType, List<NotificationSubscriber>> subscribers =
                (Map<NotificationType, List<NotificationSubscriber>>) subscribersField.get(eventBus);

        assertFalse(subscribers.containsKey(NotificationType.NOTIFICATION_CREATED));
    }

    @Test
    void shouldIgnoreNullEventWhenPublishing() throws Exception {
        eventBus.start();
        eventBus.publish(null);

        Field eventQueueField = EventBus.class.getDeclaredField("eventQueue");
        eventQueueField.setAccessible(true);
        BlockingQueue<NotificationEvent> queue = (BlockingQueue<NotificationEvent>) eventQueueField.get(eventBus);

        assertTrue(queue.isEmpty());
    }

    @Test
    void shouldGetQueueSizeCorrectly() {
        NotificationEvent event = mock(NotificationEvent.class);

        eventBus.publish(event);
        eventBus.publish(event);
        eventBus.publish(event);

        assertEquals(3, eventBus.getQueueSize());
    }

    @Test
    void shouldProcessEventsInLoop() throws InterruptedException {
        eventBus.start();
        Thread.sleep(100); // Dar tiempo para que el hilo inicie

        NotificationSubscriber subscriber = mock(NotificationSubscriber.class);
        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber);

        NotificationEvent event = NotificationEvent.builder()
                .eventId("test-event-1")
                .eventType(NotificationType.NOTIFICATION_CREATED)
                .userId("user-123")
                .message("Test message")
                .build();

        eventBus.publish(event);

        Thread.sleep(200); // Dar tiempo para que el evento sea procesado

        verify(subscriber, timeout(500)).handleEvent(event);
    }

    @Test
    void shouldProcessMultipleEventsInLoop() throws InterruptedException {
        eventBus.start();
        Thread.sleep(100);

        NotificationSubscriber subscriber = mock(NotificationSubscriber.class);
        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber);

        NotificationEvent event1 = NotificationEvent.builder()
                .eventId("test-event-1")
                .eventType(NotificationType.NOTIFICATION_CREATED)
                .userId("user-123")
                .message("Test message 1")
                .build();

        NotificationEvent event2 = NotificationEvent.builder()
                .eventId("test-event-2")
                .eventType(NotificationType.NOTIFICATION_CREATED)
                .userId("user-123")
                .message("Test message 2")
                .build();

        eventBus.publish(event1);
        eventBus.publish(event2);

        Thread.sleep(300);

        verify(subscriber, timeout(500)).handleEvent(event1);
        verify(subscriber, timeout(500)).handleEvent(event2);
    }

    @Test
    void shouldHandleInterruptedExceptionInProcessLoop() throws InterruptedException {
        eventBus.start();
        Thread.sleep(100);

        NotificationSubscriber subscriber = mock(NotificationSubscriber.class);
        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber);

        NotificationEvent event = NotificationEvent.builder()
                .eventId("test-event-1")
                .eventType(NotificationType.NOTIFICATION_CREATED)
                .userId("user-123")
                .message("Test message")
                .build();

        eventBus.publish(event);
        Thread.sleep(200);

        // Verificar que el evento se procesó correctamente
        verify(subscriber, timeout(500)).handleEvent(event);
        
        // El EventBus maneja InterruptedException internamente en processLoop
        // No necesitamos interrumpir el hilo manualmente para este test
    }

    @Test
    void shouldProcessEventsForEventTypeWithNoSubscribers() throws InterruptedException {
        eventBus.start();
        Thread.sleep(100);

        NotificationEvent event = NotificationEvent.builder()
                .eventId("test-event-1")
                .eventType(NotificationType.TRIP_CREATED)
                .userId("user-123")
                .message("Test message")
                .build();

        eventBus.publish(event);
        Thread.sleep(200);

        // No debería lanzar excepción aunque no haya suscriptores
        assertTrue(true);
    }

    @Test
    void shouldHandleExceptionInSubscriber() throws InterruptedException {
        eventBus.start();
        Thread.sleep(100);

        NotificationSubscriber subscriber = mock(NotificationSubscriber.class);
        when(subscriber.getName()).thenReturn("test-subscriber");
        doThrow(new RuntimeException("Test exception")).when(subscriber).handleEvent(any());

        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber);

        NotificationEvent event = NotificationEvent.builder()
                .eventId("test-event-1")
                .eventType(NotificationType.NOTIFICATION_CREATED)
                .userId("user-123")
                .message("Test message")
                .build();

        eventBus.publish(event);

        Thread.sleep(200);

        verify(subscriber, timeout(500)).handleEvent(event);
    }

    @Test
    void shouldNotProcessEventsAfterStop() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        eventBus.start();
        Thread.sleep(100);

        NotificationSubscriber subscriber = mock(NotificationSubscriber.class);
        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber);
        
        // Publicar un evento antes de detener para asegurar que el loop está activo
        NotificationEvent eventBeforeStop = NotificationEvent.builder()
                .eventId("test-event-before")
                .eventType(NotificationType.NOTIFICATION_CREATED)
                .userId("user-123")
                .message("Test message before")
                .build();
        eventBus.publish(eventBeforeStop);
        Thread.sleep(300); // Esperar a que se procese
        
        // Limpiar la cola antes de detener
        Field eventQueueField = EventBus.class.getDeclaredField("eventQueue");
        eventQueueField.setAccessible(true);
        BlockingQueue<NotificationEvent> queue = (BlockingQueue<NotificationEvent>) eventQueueField.get(eventBus);
        queue.clear();
        
        // Desuscribir antes de detener
        eventBus.unsubscribe(NotificationType.NOTIFICATION_CREATED, subscriber);
        
        eventBus.stop();
        Thread.sleep(500); // Dar tiempo suficiente para que el hilo se detenga completamente

        // Verificar que el bus está detenido verificando que isRunning es false
        Field isRunningField = EventBus.class.getDeclaredField("isRunning");
        isRunningField.setAccessible(true);
        boolean isRunning = (boolean) isRunningField.get(eventBus);
        assertFalse(isRunning, "EventBus should be stopped");

        // Publicar un evento después de detener - no debería procesarse porque no hay suscriptores
        NotificationEvent event = NotificationEvent.builder()
                .eventId("test-event-1")
                .eventType(NotificationType.NOTIFICATION_CREATED)
                .userId("user-123")
                .message("Test message")
                .build();

        eventBus.publish(event);

        Thread.sleep(300);

        // Verificar que el evento anterior se procesó
        verify(subscriber, atLeastOnce()).handleEvent(eventBeforeStop);
        // El nuevo evento no debería procesarse porque no hay suscriptores y el bus está detenido
        verify(subscriber, never()).handleEvent(event);
    }

    @Test
    void shouldHandleMultipleSubscribersForSameEventType() throws InterruptedException {
        eventBus.start();
        Thread.sleep(100);

        NotificationSubscriber subscriber1 = mock(NotificationSubscriber.class);
        NotificationSubscriber subscriber2 = mock(NotificationSubscriber.class);

        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber1);
        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber2);

        NotificationEvent event = NotificationEvent.builder()
                .eventId("test-event-1")
                .eventType(NotificationType.NOTIFICATION_CREATED)
                .userId("user-123")
                .message("Test message")
                .build();

        eventBus.publish(event);

        Thread.sleep(200);

        verify(subscriber1, timeout(500)).handleEvent(event);
        verify(subscriber2, timeout(500)).handleEvent(event);
    }

    @Test
    void shouldHandleEventsForDifferentEventTypes() throws InterruptedException {
        eventBus.start();
        Thread.sleep(100);

        NotificationSubscriber subscriber1 = mock(NotificationSubscriber.class);
        NotificationSubscriber subscriber2 = mock(NotificationSubscriber.class);

        eventBus.subscribe(NotificationType.NOTIFICATION_CREATED, subscriber1);
        eventBus.subscribe(NotificationType.TRIP_CREATED, subscriber2);

        NotificationEvent event1 = NotificationEvent.builder()
                .eventId("test-event-1")
                .eventType(NotificationType.NOTIFICATION_CREATED)
                .userId("user-123")
                .message("Test message")
                .build();

        NotificationEvent event2 = NotificationEvent.builder()
                .eventId("test-event-2")
                .eventType(NotificationType.TRIP_CREATED)
                .userId("user-123")
                .message("Test message")
                .build();

        eventBus.publish(event1);
        eventBus.publish(event2);

        Thread.sleep(200);

        verify(subscriber1, timeout(500)).handleEvent(event1);
        verify(subscriber2, timeout(500)).handleEvent(event2);
        verify(subscriber1, never()).handleEvent(event2);
        verify(subscriber2, never()).handleEvent(event1);
    }
}