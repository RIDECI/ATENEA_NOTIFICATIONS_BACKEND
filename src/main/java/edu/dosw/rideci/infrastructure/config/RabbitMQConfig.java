package edu.dosw.rideci.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EVENTS_QUEUE = "rideci.notification.events";
    public static final String DLQ_QUEUE = "rideci.notification.events.dlq";
    public static final String EVENTS_EXCHANGE = "rideci.events.exchange";
    public static final String ROUTING_KEY = "events.notification.#";

    @Bean
    public DirectExchange eventsExchange() {
        return new DirectExchange(EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public Queue eventsQueue() {
        return QueueBuilder.durable(EVENTS_QUEUE)
                .withArgument("x-dead-letter-exchange", EVENTS_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_QUEUE).build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(eventsQueue())
                .to(eventsExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory cf) {
        return new RabbitTemplate(cf);
    }
}
