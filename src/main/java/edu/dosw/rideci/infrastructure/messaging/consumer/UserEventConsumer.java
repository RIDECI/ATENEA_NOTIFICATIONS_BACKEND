package edu.dosw.rideci.infrastructure.messaging.consumer;

import edu.dosw.rideci.application.events.listener.UserSyncFailedEvent;
import edu.dosw.rideci.application.service.ProcessUserEventService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserEventConsumer {

    private final ProcessUserEventService processUserEventService;

    public UserEventConsumer(ProcessUserEventService processUserEventService) {
        this.processUserEventService = processUserEventService;
    }

    @RabbitListener(queues = "notification.user.queue")
    public void handleUserSyncFailedEvent(UserSyncFailedEvent event) {
        try {
            log.info("Recibido UserSyncFailedEvent: userId={}, email={}, reason={}",
                    event.getUserId(), event.getEmail(), event.getReason());

            processUserEventService.processUserSyncFailed(event);

            log.info("Procesado exitosamente UserSyncFailedEvent");
        } catch (Exception e) {
            log.error("Error procesando UserSyncFailedEvent: {}", e.getMessage(), e);
            throw e;
        }
    }
}