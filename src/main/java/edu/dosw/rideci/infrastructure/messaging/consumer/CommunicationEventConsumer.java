package edu.dosw.rideci.infrastructure.messaging.consumer;

import edu.dosw.rideci.application.events.ReportCreatedEvent;
import edu.dosw.rideci.application.events.ConversationCreatedEvent;
import edu.dosw.rideci.application.events.MessageSentEvent;
import edu.dosw.rideci.application.service.SecurityEventService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommunicationEventConsumer {

    private final SecurityEventService securityEventService;

    public CommunicationEventConsumer(SecurityEventService securityEventService) {
        this.securityEventService = securityEventService;
    }

    @RabbitListener(queues = "notification.report.created.queue")
    public void handleReportCreated(ReportCreatedEvent event) {
        try {
            log.info("Recibido ReportCreatedEvent: reportId={}, type={}, userId={}, targetId={}",
                    event.getReportId(), event.getType(), event.getUserId(), event.getTargetId());

            securityEventService.processReportCreated(event);

            log.info("Procesado exitosamente ReportCreatedEvent");
        } catch (Exception e) {
            log.error("Error procesando ReportCreatedEvent: {}", e.getMessage(), e);
            throw e;
        }
    }

    @RabbitListener(queues = "notification.conversation.created.queue")
    public void handleConversationCreated(ConversationCreatedEvent event) {
        try {
            log.info("Recibido ConversationCreatedEvent: conversationId={}, travelId={}, type={}",
                    event.getConversationId(), event.getTravelId(), event.getType());

            securityEventService.processConversationCreated(event);

            log.info("Procesado exitosamente ConversationCreatedEvent");
        } catch (Exception e) {
            log.error("Error procesando ConversationCreatedEvent: {}", e.getMessage(), e);
            throw e;
        }
    }

    @RabbitListener(queues = "notification.message.sent.queue")
    public void handleMessageSent(MessageSentEvent event) {
        try {
            log.info("Recibido MessageSentEvent: conversationId={}, senderId={}, receiverId={}",
                    event.getConversationId(), event.getSenderId(), event.getReceiverId());

            securityEventService.processMessageSent(event);

            log.info("Procesado exitosamente MessageSentEvent");
        } catch (Exception e) {
            log.error("Error procesando MessageSentEvent: {}", e.getMessage(), e);
            throw e;
        }
    }
}