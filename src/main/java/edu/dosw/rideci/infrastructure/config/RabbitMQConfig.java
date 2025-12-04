package edu.dosw.rideci.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String RIDECI_EVENTS_EXCHANGE = "rideci.events.exchange";
    public static final String USER_EXCHANGE = "user.exchange";

    public static final String NOTIFICATION_EVENTS_QUEUE = "rideci.notification.events";
    public static final String NOTIFICATION_USER_QUEUE = "notification.user.queue";
    public static final String DLQ_QUEUE = "rideci.notification.events.dlq";

    public static final String NOTIFICATION_ROUTING_KEY = "events.notification.#";
    public static final String USER_SYNC_FAILED_ROUTING_KEY = "user.sync.failed";

    @Bean
    public TopicExchange rideciEventsExchange() {
        return new TopicExchange(RIDECI_EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE, true, false);
    }

    @Bean
    public Queue notificationEventsQueue() {
        return QueueBuilder.durable(NOTIFICATION_EVENTS_QUEUE)
                .withArgument("x-dead-letter-exchange", RIDECI_EVENTS_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue notificationUserQueue() {
        return QueueBuilder.durable(NOTIFICATION_USER_QUEUE)
                .withArgument("x-dead-letter-exchange", USER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_QUEUE).build();
    }

    @Bean
    public Binding notificationEventsBinding() {
        return BindingBuilder.bind(notificationEventsQueue())
                .to(rideciEventsExchange())
                .with(NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public Binding userSyncFailedBinding() {
        return BindingBuilder.bind(notificationUserQueue())
                .to(userExchange())
                .with(USER_SYNC_FAILED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}