package edu.dosw.rideci.application.events.listener;

import edu.dosw.rideci.application.service.ProcessUserEventService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventsListener {
    private final ProcessUserEventService processUserEventService;

    public UserEventsListener(ProcessUserEventService processUserEventService) {
        this.processUserEventService = processUserEventService;
    }

    @RabbitListener(queues = "user.created.queue")
    public void handleUserCreated(UserCreatedEvent event) {
    }

    @RabbitListener(queues = "user.sync.failed.queue")
    public void handleUserSyncFailed(UserSyncFailedEvent event) {
        processUserEventService.processUserSyncFailed(event);
    }
}