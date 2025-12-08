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

    // === EXCHANGES QUE YA EXISTEN (declarados por otros servicios) ===
    // Solo necesitas saber los nombres, NO necesitas declarar los beans

    // Nombres de exchanges que usan tus compañeros
    public static final String USER_EXCHANGE = "user.exchange";
    public static final String PROFILE_EXCHANGE = "profile.exchange";
    public static final String TRAVEL_EXCHANGE = "travel.exchange";
    public static final String ADMIN_EXCHANGE = "admin.exchange";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String REPORT_EXCHANGE = "rideci.report.exchange";
    public static final String CHAT_EXCHANGE = "rideci.chat.exchange";
    public static final String CONVERSATION_EXCHANGE = "rideci.conversation.exchange";
    public static final String BOOKING_EXCHANGE = "booking.exchange";

    // === TUS PROPIAS COLAS (solo las que TÚ vas a usar) ===

    // 1. Cola para TODOS los eventos de usuario
    public static final String NOTIF_USER_EVENTS_QUEUE = "notif.user.events.queue";

    // 2. Cola para TODOS los eventos de perfil
    public static final String NOTIF_PROFILE_EVENTS_QUEUE = "notif.profile.events.queue";

    // 3. Cola para TODOS los eventos de viajes
    public static final String NOTIF_TRAVEL_EVENTS_QUEUE = "notif.travel.events.queue";

    // 4. Cola para TODOS los eventos administrativos
    public static final String NOTIF_ADMIN_EVENTS_QUEUE = "notif.admin.events.queue";

    // 5. Cola para TODOS los eventos de pagos
    public static final String NOTIF_PAYMENT_EVENTS_QUEUE = "notif.payment.events.queue";

    // 6. Cola para TODOS los eventos de reportes/chat
    public static final String NOTIF_COMMUNICATION_EVENTS_QUEUE = "notif.communication.events.queue";

    // === SOLO DECLARA TUS COLAS Y HAZ BINDING A LOS EXISTENTES ===

    // 1. Cola para eventos de User
    @Bean
    public Queue notifUserEventsQueue() {
        return QueueBuilder.durable(NOTIF_USER_EVENTS_QUEUE).build();
    }

    @Bean
    public Binding bindNotifUserEvents() {
        // Binding al exchange que YA EXISTE
        return BindingBuilder
                .bind(notifUserEventsQueue())
                .to(new TopicExchange(USER_EXCHANGE))  // Exchange existente
                .with("auth.user.#");
    }

    // 2. Cola para eventos de Profile
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

    // 3. Cola para eventos de Travel
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

    // 4. Cola para eventos de Admin
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

    // 5. Cola para eventos de Payment
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

    // 6. Cola para eventos de Report/Chat/Conversation
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

    // 7. Cola para eventos de Booking
    @Bean
    public Binding bindNotifBookingEvents() {
        return BindingBuilder
                .bind(notifTravelEventsQueue())  // Reusa la cola de viajes si quieres
                .to(new TopicExchange(BOOKING_EXCHANGE))
                .with("booking.#");
    }

    // === CONFIGURACIÓN GENERAL ===

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