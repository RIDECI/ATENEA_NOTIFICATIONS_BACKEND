package edu.dosw.rideci.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchanges
    public static final String RIDECI_EVENTS_EXCHANGE = "rideci.events.exchange";
    public static final String USER_EXCHANGE = "user.exchange";
    public static final String TRAVEL_EXCHANGE = "travel.exchange";
    public static final String REPORT_EXCHANGE = "rideci.report.exchange";
    public static final String CONVERSATION_EXCHANGE = "rideci.conversation.exchange";
    public static final String CHAT_EXCHANGE = "rideci.chat.exchange";

    // Queues existentes
    public static final String NOTIFICATION_EVENTS_QUEUE = "rideci.notification.events";
    public static final String NOTIFICATION_USER_QUEUE = "notification.user.queue";
    public static final String DLQ_QUEUE = "rideci.notification.events.dlq";

    // Queues de viajes
    public static final String NOTIFICATION_TRAVEL_CREATED_QUEUE = "notification.travel.created.queue";
    public static final String NOTIFICATION_TRAVEL_UPDATED_QUEUE = "notification.travel.updated.queue";
    public static final String NOTIFICATION_TRAVEL_CANCELLED_QUEUE = "notification.travel.cancelled.queue";
    public static final String NOTIFICATION_TRAVEL_COMPLETED_QUEUE = "notification.travel.completed.queue";

    // Queues de comunicación
    public static final String NOTIFICATION_REPORT_CREATED_QUEUE = "notification.report.created.queue";
    public static final String NOTIFICATION_CONVERSATION_CREATED_QUEUE = "notification.conversation.created.queue";
    public static final String NOTIFICATION_MESSAGE_SENT_QUEUE = "notification.message.sent.queue";

    // Routing keys existentes
    public static final String NOTIFICATION_ROUTING_KEY = "events.notification.#";
    public static final String USER_SYNC_FAILED_ROUTING_KEY = "user.sync.failed";

    // Routing keys de viajes
    public static final String TRAVEL_CREATED_ROUTING_KEY = "travel.created";
    public static final String TRAVEL_UPDATED_ROUTING_KEY = "travel.updated";
    public static final String TRAVEL_CANCELLED_ROUTING_KEY = "travel.cancelled";
    public static final String TRAVEL_COMPLETED_ROUTING_KEY = "travel.completed";

    // Routing keys de comunicación
    public static final String REPORT_CREATED_ROUTING_KEY = "report.created";
    public static final String CONVERSATION_CREATED_ROUTING_KEY = "conversation.created";
    public static final String CHAT_MESSAGE_ROUTING_KEY = "chat.message";

    // Routing key de restablecimiento de contraseña
    public static final String PASSWORD_EXCHANGE = "rideci.password.exchange";
    public static final String NOTIFICATION_PASSWORD_RESET_QUEUE = "notification.password.reset.queue";
    public static final String PASSWORD_RESET_ROUTING_KEY = "password.reset";

    // Exchanges
    @Bean
    public TopicExchange rideciEventsExchange() {
        return new TopicExchange(RIDECI_EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange travelExchange() {
        return new TopicExchange(TRAVEL_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange reportExchange() {
        return new TopicExchange(REPORT_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange conversationExchange() {
        return new TopicExchange(CONVERSATION_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange chatExchange() {
        return new TopicExchange(CHAT_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange passwordExchange() {
        return new TopicExchange(PASSWORD_EXCHANGE, true, false);
    }


    // Queues existentes
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

    // Queues de viajes
    @Bean
    public Queue notificationTravelCreatedQueue() {
        return QueueBuilder.durable(NOTIFICATION_TRAVEL_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", TRAVEL_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue notificationTravelUpdatedQueue() {
        return QueueBuilder.durable(NOTIFICATION_TRAVEL_UPDATED_QUEUE)
                .withArgument("x-dead-letter-exchange", TRAVEL_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue notificationTravelCancelledQueue() {
        return QueueBuilder.durable(NOTIFICATION_TRAVEL_CANCELLED_QUEUE)
                .withArgument("x-dead-letter-exchange", TRAVEL_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue notificationTravelCompletedQueue() {
        return QueueBuilder.durable(NOTIFICATION_TRAVEL_COMPLETED_QUEUE)
                .withArgument("x-dead-letter-exchange", TRAVEL_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    // Queues de comunicación
    @Bean
    public Queue notificationReportCreatedQueue() {
        return QueueBuilder.durable(NOTIFICATION_REPORT_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", REPORT_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue notificationConversationCreatedQueue() {
        return QueueBuilder.durable(NOTIFICATION_CONVERSATION_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", CONVERSATION_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue notificationMessageSentQueue() {
        return QueueBuilder.durable(NOTIFICATION_MESSAGE_SENT_QUEUE)
                .withArgument("x-dead-letter-exchange", CHAT_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_QUEUE).build();
    }

    // Queue de restablecimiento de contraseña
    @Bean
    public Queue notificationPasswordResetQueue() {
        return QueueBuilder.durable(NOTIFICATION_PASSWORD_RESET_QUEUE)
                .withArgument("x-dead-letter-exchange", PASSWORD_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }


    // Bindings existentes
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

    // Bindings de viajes
    @Bean
    public Binding travelCreatedBinding() {
        return BindingBuilder.bind(notificationTravelCreatedQueue())
                .to(travelExchange())
                .with(TRAVEL_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding travelUpdatedBinding() {
        return BindingBuilder.bind(notificationTravelUpdatedQueue())
                .to(travelExchange())
                .with(TRAVEL_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding travelCancelledBinding() {
        return BindingBuilder.bind(notificationTravelCancelledQueue())
                .to(travelExchange())
                .with(TRAVEL_CANCELLED_ROUTING_KEY);
    }

    @Bean
    public Binding travelCompletedBinding() {
        return BindingBuilder.bind(notificationTravelCompletedQueue())
                .to(travelExchange())
                .with(TRAVEL_COMPLETED_ROUTING_KEY);
    }

    @Bean
    public Binding reportCreatedBinding() {
        return BindingBuilder.bind(notificationReportCreatedQueue())
                .to(reportExchange())
                .with(REPORT_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding conversationCreatedBinding() {
        return BindingBuilder.bind(notificationConversationCreatedQueue())
                .to(conversationExchange())
                .with(CONVERSATION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding messageSentBinding() {
        return BindingBuilder.bind(notificationMessageSentQueue())
                .to(chatExchange())
                .with(CHAT_MESSAGE_ROUTING_KEY);
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

    // Binding de restablecimiento de contraseña
    @Bean
    public Binding passwordResetBinding() {
        return BindingBuilder.bind(notificationPasswordResetQueue())
                .to(passwordExchange())
                .with(PASSWORD_RESET_ROUTING_KEY);
    }
}