package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.events.PasswordResetEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String PASSWORD_EXCHANGE = "rideci.password.exchange";
    private static final String PASSWORD_RESET_ROUTING_KEY = "password.reset";

    public void publishPasswordResetEvent(String email, String resetCode, int expiryMinutes) {
        PasswordResetEvent event = PasswordResetEvent.builder()
                .email(email)
                .resetCode(resetCode)
                .expiryDate(LocalDateTime.now().plusMinutes(expiryMinutes))
                .expiryMinutes(expiryMinutes)
                .build();

        rabbitTemplate.convertAndSend(PASSWORD_EXCHANGE, PASSWORD_RESET_ROUTING_KEY, event);

        log.info("Password reset event published for email: {}", email);
    }
}