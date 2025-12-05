package edu.dosw.rideci.infrastructure.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.rideci.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final EventBus eventBus;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.EVENTS_QUEUE)
    public void onMessage(String message) {
        try {
            JsonNode json = objectMapper.readTree(message);
            EventType type = EventType.valueOf(json.get("type").asText());

            NotificationEvent event = NotificationEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType(type)
                    .userId(json.path("userId").asText(null))
                    .message(json.path("message").asText(null))
                    .priority(priorityFor(type))
                    .timestamp(Instant.now())
                    .payload(json.path("payload").toString())
                    .build();

            log.info("Received event from RabbitMQ: {}", event);
            eventBus.publish(event);

        } catch (Exception e) {
            log.error("Error processing RabbitMQ message: {}", message, e);
        }
    }

    private int priorityFor(EventType type) {
        return switch (type) {
            case EMERGY_BOTON, SECURITY_INCIDENT, LOCATION_ALERT -> 1;
            case TRIP_CREATED, TRIP_UPDATED, TRIP_CANCELLED, TRIP_COMPLETED,
                 PAYMENT_CONFIRMED, PAYMENT_FAILED,
                 DRIVER_VALIDATED, NEW_DISTINTIVE -> 2;
            default -> 3;
        };
    }
}
