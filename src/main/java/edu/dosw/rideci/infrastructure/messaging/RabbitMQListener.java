package edu.dosw.rideci.infrastructure.messaging;

import edu.dosw.rideci.application.port.in.SendEmailNotificationUseCase;
import edu.dosw.rideci.domain.model.Enum.EmailType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Listener that receives events from RabbitMQ and triggers email notifications.
 * This class delegates the sending logic to SendEmailNotificationUseCase,
 * maintaining hexagonal architecture.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final SendEmailNotificationUseCase sendEmailNotificationUseCase;

    /**
     * Listens for email notification events sent through RabbitMQ.
     * The message should contain:
     *   - userId
     *   - emailType
     *   - templateData
     */
    @RabbitListener(queues = "rideci.email.notifications")
    public void onEmailNotificationEvent(EmailEventMessage message) {
        log.info("Received Email Event from RabbitMQ: {}", message);

        try {
            sendEmailNotificationUseCase.sendEmail(
                    UUID.fromString(message.userId()),
                    message.emailType(),
                    message.templateData()
            );

            log.info("Email notification successfully processed for user {}", message.userId());

        } catch (Exception ex) {
            log.error("Failed to process email event for user {} - Error: {}",
                    message.userId(), ex.getMessage());
        }
    }

    /**
     * Event message format received from RabbitMQ.
     */
    public record EmailEventMessage(
            String userId,
            EmailType emailType,
            Map<String, String> templateData
    ) {}
}
