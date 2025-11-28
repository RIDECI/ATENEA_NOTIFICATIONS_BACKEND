package edu.dosw.rideci.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de infraestructura para la integración con RabbitMQ.
 * Define colas, exchange, binding y el {@link RabbitTemplate} utilizado
 * para el envío y recepción de mensajes de eventos de notificación.
 *
 * Maneja también una Dead Letter Queue (DLQ) para mensajes que no
 * puedan ser procesados correctamente.
 *
 * @author RideECI
 * @version 1.0
 */
@Configuration
public class RabbitMQConfig {

    /** Nombre de la cola principal de eventos de notificación. */
    public static final String EVENTS_QUEUE = "rideci.notification.events";

    /** Nombre de la cola de mensajes muertos (DLQ) para eventos de notificación. */
    public static final String DLQ_QUEUE = "rideci.notification.events.dlq";

    /** Nombre del exchange utilizado para los eventos. */
    public static final String EVENTS_EXCHANGE = "rideci.events.exchange";

    /** Routing key para enrutar eventos de notificación. */
    public static final String ROUTING_KEY = "events.notification.#";

    /**
     * Define el exchange directo usado para enrutar los mensajes de eventos.
     *
     * @return Exchange de tipo {@link DirectExchange} durable.
     */
    @Bean
    public DirectExchange eventsExchange() {
        return new DirectExchange(EVENTS_EXCHANGE, true, false);
    }

    /**
     * Define la cola principal de eventos de notificación.
     * Configura la cola con soporte para Dead Letter Exchange (DLX).
     *
     * @return Cola durable para eventos de notificación.
     */
    @Bean
    public Queue eventsQueue() {
        return QueueBuilder.durable(EVENTS_QUEUE)
                .withArgument("x-dead-letter-exchange", EVENTS_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    /**
     * Define la cola de mensajes muertos (DLQ) asociada a los eventos de notificación.
     *
     * @return Cola durable para mensajes no procesados correctamente.
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_QUEUE).build();
    }

    /**
     * Crea el binding entre la cola principal de eventos y el exchange,
     * usando la routing key configurada.
     *
     * @return Binding entre {@link #eventsQueue()} y {@link #eventsExchange()}.
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(eventsQueue())
                .to(eventsExchange())
                .with(ROUTING_KEY);
    }

    /**
     * Configura el {@link RabbitTemplate} que se usará para publicar mensajes
     * en RabbitMQ utilizando la {@link CachingConnectionFactory} proporcionada.
     *
     * @param cf Connection factory de RabbitMQ con cacheo de conexiones.
     * @return Instancia configurada de {@link RabbitTemplate}.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory cf) {
        return new RabbitTemplate(cf);
    }
}
