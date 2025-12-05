package edu.dosw.rideci.infrastructure.messaging.consumer;

import edu.dosw.rideci.application.events.TravelCreatedEvent;
import edu.dosw.rideci.application.events.TravelCompletedEvent;
import edu.dosw.rideci.application.service.ProcessUserEventService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TravelEventConsumer {

    private final ProcessUserEventService processUserEventService;

    public TravelEventConsumer(ProcessUserEventService processUserEventService) {
        this.processUserEventService = processUserEventService;
    }

    @RabbitListener(queues = "notification.travel.created.queue")
    public void handleTravelCreatedEvent(TravelCreatedEvent event) {
        try {
            log.info("Recibido TravelCreatedEvent: travelId={}, driverId={}, organizerId={}",
                    event.getTravelId(), event.getDriverId(), event.getOrganizerId());

            processUserEventService.processTravelCreated(event);

            log.info("Procesado exitosamente TravelCreatedEvent");
        } catch (Exception e) {
            log.error("Error procesando TravelCreatedEvent: {}", e.getMessage(), e);
            throw e;
        }
    }

    @RabbitListener(queues = "notification.travel.completed.queue")
    public void handleTravelCompletedEvent(TravelCompletedEvent event) {
        try {
            log.info("Recibido TravelCompletedEvent: travelId={}, driverId={}, state={}",
                    event.getTravelId(), event.getDriverId(), event.getState());

            processUserEventService.processTravelCompleted(event);

            log.info("Procesado exitosamente TravelCompletedEvent");
        } catch (Exception e) {
            log.error("Error procesando TravelCompletedEvent: {}", e.getMessage(), e);
            throw e;
        }
    }
}