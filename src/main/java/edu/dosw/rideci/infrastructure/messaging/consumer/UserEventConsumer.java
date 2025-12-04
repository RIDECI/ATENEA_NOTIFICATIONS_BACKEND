package edu.dosw.rideci.infrastructure.messaging.consumer;

import edu.dosw.rideci.application.events.listener.UserSyncFailedEvent;
import edu.dosw.rideci.application.service.ProcessUserEventService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    private final ProcessUserEventService processUserEventService;

    public UserEventConsumer(ProcessUserEventService processUserEventService) {
        this.processUserEventService = processUserEventService;
    }

    @RabbitListener(queues = "notification.user.queue")
    public void handleUserSyncFailedEvent(UserSyncFailedEvent event) {
        processUserEventService.processUserSyncFailed(event);
    }
}