package edu.dosw.rideci.application.events.listener;

import edu.dosw.rideci.application.events.ReportCreatedEvent;
import edu.dosw.rideci.application.service.SecurityEventService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SecurityEventsListener {
    private final SecurityEventService securityEventService;

    public SecurityEventsListener(SecurityEventService securityEventService) {
        this.securityEventService = securityEventService;
    }

    @RabbitListener(queues = "report.created.queue")
    public void handleReportCreated(ReportCreatedEvent event) {
        securityEventService.processReportCreated(event);
    }
}