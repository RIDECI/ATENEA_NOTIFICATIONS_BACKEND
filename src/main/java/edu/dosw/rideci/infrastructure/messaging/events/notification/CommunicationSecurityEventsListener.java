package edu.dosw.rideci.infrastructure.messaging.events.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.rideci.infrastructure.config.RabbitMQConfig;
import edu.dosw.rideci.infrastructure.messaging.events.communication_security.MessageSentEvent;
import edu.dosw.rideci.infrastructure.messaging.events.communication_security.ReportCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommunicationSecurityEventsListener {

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.NOTIF_COMMUNICATION_EVENTS_QUEUE)
    public void handleCommunicationEvent(Message message) {
        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            String exchange = message.getMessageProperties().getReceivedExchange();

            log.info("📨 Evento recibido desde {} con routing key: {}", exchange, routingKey);

            switch (routingKey) {
                case "chat.message":
                    handleChatMessage(message);
                    break;

                case "report.created":
                    handleReportCreated(message);
                    break;

                default:
                    log.debug("Routing key no manejado: {}", routingKey);
            }

        } catch (Exception e) {
            log.error("❌ Error procesando evento de comunicación/seguridad", e);
        }
    }

    private void handleChatMessage(Message message) throws Exception {
        MessageSentEvent event = objectMapper.readValue(message.getBody(), MessageSentEvent.class);

        log.info("💬 Procesando mensaje de chat - ID: {}", event.getMessageId());
        log.info("De: {} (ID: {})", event.getSenderName(), event.getSenderId());
        log.info("Para: {} (ID: {})",
                event.getReceiverName() != null ? event.getReceiverName() : "Grupo",
                event.getReceiverId());
        log.info("Contenido: {}", event.getContent());
        log.info("Tipo: {}, Es emergencia: {}", event.getMessageType(), event.getIsEmergency());
        log.info("Requiere notificación: {}", event.getRequiresNotification());

        // Aquí en el futuro se llamará al servicio de notificaciones
        // Por ahora solo registramos el evento
    }

    private void handleReportCreated(Message message) throws Exception {
        ReportCreatedEvent event = objectMapper.readValue(message.getBody(), ReportCreatedEvent.class);

        log.info("🚨 Procesando reporte creado - ID: {}", event.getReportId());
        log.info("Usuario: {} (ID: {})", event.getUserName(), event.getUserId());
        log.info("Email: {}", event.getUserEmail());
        log.info("Tipo: {}, Subtipo: {}", event.getReportType(), event.getSubtype());
        log.info("Severidad: {}, Prioridad: {}", event.getSeverity(), event.getPriority());
        log.info("Título: {}", event.getTitle());
        log.info("Descripción: {}", event.getDescription());
        log.info("Requiere acción inmediata: {}", event.getRequiresImmediateAction());
        log.info("Estado: {}", event.getStatus());

        // Aquí en el futuro se llamará al servicio de notificaciones
        // Por ahora solo registramos el evento
    }
}