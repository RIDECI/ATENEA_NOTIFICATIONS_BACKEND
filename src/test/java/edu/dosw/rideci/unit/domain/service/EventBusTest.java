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
}