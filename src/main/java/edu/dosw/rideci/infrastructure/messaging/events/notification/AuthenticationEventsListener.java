package edu.dosw.rideci.infrastructure.messaging.events.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.rideci.infrastructure.config.RabbitMQConfig;
import edu.dosw.rideci.infrastructure.messaging.events.authentication.PasswordResetEvent;
import edu.dosw.rideci.infrastructure.messaging.events.authentication.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationEventsListener {

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.NOTIF_USER_EVENTS_QUEUE)
    public void handleAuthenticationEvent(Message message) {
        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            String exchange = message.getMessageProperties().getReceivedExchange();

            log.info("🔐 Evento de Autenticación recibido desde {}: {}", exchange, routingKey);

            switch (routingKey) {
                case "auth.user.created":
                case "auth.user.updated":
                    handleUserEvent(message, routingKey);
                    break;

                case "auth.user.password.reset":
                    handlePasswordResetEvent(message);
                    break;

                default:
                    log.debug("Routing key no manejado en autenticación: {}", routingKey);
            }

        } catch (Exception e) {
            log.error("❌ Error procesando evento de autenticación", e);
        }
    }

    private void handleUserEvent(Message message, String routingKey) throws Exception {
        UserEvent event = objectMapper.readValue(message.getBody(), UserEvent.class);

        log.info("👤 Evento de Usuario - Tipo: {}", routingKey);
        log.info("   ID: {}", event.getUserId());
        log.info("   Nombre: {}", event.getName());
        log.info("   Email: {}", event.getEmail());
        log.info("   Rol: {}", event.getRole());
        log.info("   Evento: {}", event.getEventType());

        if ("auth.user.created".equals(routingKey)) {
            log.info("   🎉 Nuevo usuario registrado - Enviar email de bienvenida");
            // Aquí se llamará al servicio para enviar email de bienvenida
        } else if ("auth.user.updated".equals(routingKey)) {
            log.info("   📝 Usuario actualizado - Enviar email de confirmación");
            // Aquí se llamará al servicio para enviar email de confirmación de cambios
        }
    }

    private void handlePasswordResetEvent(Message message) throws Exception {
        PasswordResetEvent event = objectMapper.readValue(message.getBody(), PasswordResetEvent.class);

        log.info("🔑 Evento de Restablecimiento de Contraseña");
        log.info("   Usuario: {} ({})", event.getUserName(), event.getEmail());
        log.info("   Token/Code: {}", event.getResetCode() != null ? event.getResetCode() : event.getResetToken());
        log.info("   Expira: {} (en {} minutos)", event.getExpiryDate(), event.getExpiryMinutes());

        // Aquí se llamará al servicio para enviar email con el enlace/código de restablecimiento
        log.info("   📧 Email de restablecimiento listo para enviar");
    }
}