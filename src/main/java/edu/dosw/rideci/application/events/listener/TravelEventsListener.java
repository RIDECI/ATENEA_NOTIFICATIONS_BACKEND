package edu.dosw.rideci.application.events.listener;

import edu.dosw.rideci.application.events.TravelCreatedEvent;
import edu.dosw.rideci.application.events.TravelUpdatedEvent;
import edu.dosw.rideci.application.events.TravelCancelledEvent;
import edu.dosw.rideci.application.events.TravelCompletedEvent;
import edu.dosw.rideci.application.service.ProcessUserEventService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TravelEventsListener {
    private final ProcessUserEventService processUserEventService;

    public TravelEventsListener(ProcessUserEventService processUserEventService) {
        this.processUserEventService = processUserEventService;
    }

    @RabbitListener(queues = "travel.created.queue")
    public void handleTravelCreated(TravelCreatedEvent event) {
        processUserEventService.processTravelCreated(event);
    }

    @RabbitListener(queues = "travel.updated.queue")
    public void handleTravelUpdated(TravelUpdatedEvent event) {
        processUserEventService.processTravelUpdated(event);
    }

    @RabbitListener(queues = "travel.cancelled.queue")
    public void handleTravelCancelled(TravelCancelledEvent event) {
        processUserEventService.processTravelCancelled(event);
    }

    @RabbitListener(queues = "travel.completed.queue")
    public void handleTravelCompleted(TravelCompletedEvent event) {
        processUserEventService.processTravelCompleted(event);
    }
}