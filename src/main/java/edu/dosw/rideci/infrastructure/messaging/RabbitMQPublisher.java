package edu.dosw.rideci.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.rideci.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public void publish(String routingKey, Object payload) {
        try {
            String json = mapper.writeValueAsString(payload);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EVENTS_EXCHANGE,
                    routingKey,
                    json
            );

            log.info("Published event to RabbitMQ: {}", json);
        } catch (Exception e) {
            log.error("Error publishing event to RabbitMQ", e);
        }
    }
}
