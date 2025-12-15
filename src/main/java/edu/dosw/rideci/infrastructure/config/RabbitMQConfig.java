package edu.dosw.rideci.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_EXCHANGE = "user.exchange";
    public static final String PROFILE_EXCHANGE = "profile.exchange";
    public static final String TRAVEL_EXCHANGE = "travel.exchange";
    public static final String ADMIN_EXCHANGE = "admin.exchange";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String REPORT_EXCHANGE = "rideci.report.exchange";
    public static final String CHAT_EXCHANGE = "rideci.chat.exchange";
    public static final String CONVERSATION_EXCHANGE = "rideci.conversation.exchange";
    public static final String BOOKING_EXCHANGE = "booking.exchange";

    public static final String NOTIF_USER_EVENTS_QUEUE = "notif.user.events.queue";

    public static final String NOTIF_PROFILE_EVENTS_QUEUE = "notif.profile.events.queue";

    public static final String NOTIF_TRAVEL_EVENTS_QUEUE = "notif.travel.events.queue";

    public static final String NOTIF_ADMIN_EVENTS_QUEUE = "notif.admin.events.queue";

    public static final String NOTIF_PAYMENT_EVENTS_QUEUE = "notif.payment.events.queue";

    public static final String NOTIF_COMMUNICATION_EVENTS_QUEUE = "notif.communication.events.queue";

    @Bean
    public Queue notifUserEventsQueue() {
        return QueueBuilder.durable(NOTIF_USER_EVENTS_QUEUE).build();
    }

    @Bean
    public Binding bindNotifUserEvents() {
        return BindingBuilder
                .bind(notifUserEventsQueue())
                .to(new TopicExchange(USER_EXCHANGE))  // Exchange existente
                .with("auth.user.#");
    }

    @Bean
    public Queue notifProfileEventsQueue() {
        return QueueBuilder.durable(NOTIF_PROFILE_EVENTS_QUEUE).build();
    }

    @Bean
    public Binding bindNotifProfileEvents() {
        return BindingBuilder
                .bind(notifProfileEventsQueue())
                .to(new TopicExchange(PROFILE_EXCHANGE))
                .with("profile.#");
    }

    @Bean
    public Queue notifTravelEventsQueue() {
        return QueueBuilder.durable(NOTIF_TRAVEL_EVENTS_QUEUE).build();
    }

    @Bean
    public Binding bindNotifTravelEvents() {
        return BindingBuilder
                .bind(notifTravelEventsQueue())
                .to(new TopicExchange(TRAVEL_EXCHANGE))
                .with("travel.#");
    }

    @Bean
    public Queue notifAdminEventsQueue() {
        return QueueBuilder.durable(NOTIF_ADMIN_EVENTS_QUEUE).build();
    }

    @Bean
    public Binding bindNotifAdminEvents() {
        return BindingBuilder
                .bind(notifAdminEventsQueue())
                .to(new TopicExchange(ADMIN_EXCHANGE))
                .with("admin.#");
    }

    @Bean
    public Queue notifPaymentEventsQueue() {
        return QueueBuilder.durable(NOTIF_PAYMENT_EVENTS_QUEUE).build();
    }

    @Bean
    public Binding bindNotifPaymentEvents() {
        return BindingBuilder
                .bind(notifPaymentEventsQueue())
                .to(new TopicExchange(PAYMENT_EXCHANGE))
                .with("payment.#");
    }

    @Bean
    public Queue notifCommunicationEventsQueue() {
        return QueueBuilder.durable(NOTIF_COMMUNICATION_EVENTS_QUEUE).build();
    }

    @Bean
    public Binding bindNotifReportEvents() {
        return BindingBuilder
                .bind(notifCommunicationEventsQueue())
                .to(new TopicExchange(REPORT_EXCHANGE))
                .with("report.#");
    }

    @Bean
    public Binding bindNotifChatEvents() {
        return BindingBuilder
                .bind(notifCommunicationEventsQueue())
                .to(new TopicExchange(CHAT_EXCHANGE))
                .with("chat.#");
    }

    @Bean
    public Binding bindNotifConversationEvents() {
        return BindingBuilder
                .bind(notifCommunicationEventsQueue())
                .to(new TopicExchange(CONVERSATION_EXCHANGE))
                .with("conversation.#");
    }

    @Bean
    public Binding bindNotifBookingEvents() {
        return BindingBuilder
                .bind(notifTravelEventsQueue())
                .to(new TopicExchange(BOOKING_EXCHANGE))
                .with("booking.#");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(converter);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setPrefetchCount(10);
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
        RabbitAdmin admin = new RabbitAdmin(cf);
        admin.setAutoStartup(true);
        return admin;
    }
}