package edu.dosw.rideci.application.events.listener;

import edu.dosw.rideci.application.events.PasswordResetEvent;
import edu.dosw.rideci.application.port.in.SendPasswordRecoveryEmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PasswordResetEventListener {

    private final SendPasswordRecoveryEmailUseCase sendPasswordRecoveryEmailUseCase;

    @RabbitListener(queues = "password.reset.queue")
    public void handlePasswordResetEvent(PasswordResetEvent event) {
        try {
            var command = new SendPasswordRecoveryEmailUseCase.PasswordRecoveryCommand(
                    event.getEmail(),
                    event.getResetCode(),
                    Map.of(
                            "expiryDate", event.getExpiryDate().toString(),
                            "expiryMinutes", String.valueOf(event.getExpiryMinutes())
                    )
            );

            sendPasswordRecoveryEmailUseCase.sendPasswordRecoveryEmail(command);

        } catch (Exception e) {
            // Log the error and potentially send to DLQ
            throw new RuntimeException("Error processing password reset event", e);
        }
    }
}