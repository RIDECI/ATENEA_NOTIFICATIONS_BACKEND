package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.SendPasswordRecoveryEmailUseCase;
import edu.dosw.rideci.application.port.out.EmailNotificationPort;
import edu.dosw.rideci.application.port.out.EmailTemplateRepositoryPort;
import edu.dosw.rideci.application.port.out.UserRepositoryPort;
import edu.dosw.rideci.domain.model.EmailNotification;
import edu.dosw.rideci.domain.model.EmailTemplate;
import edu.dosw.rideci.domain.model.Enum.EmailSendStatus;
import edu.dosw.rideci.domain.model.Enum.EmailType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendPasswordRecoveryService implements SendPasswordRecoveryEmailUseCase {

    private final EmailNotificationPort emailNotificationPort;
    private final UserRepositoryPort userRepositoryPort;
    private final EmailTemplateRepositoryPort emailTemplateRepositoryPort;
    private final TemplateProcessorService templateProcessorService;

    @Override
    public void sendPasswordRecoveryEmail(PasswordRecoveryCommand command) {
        log.info("Processing password recovery email for React frontend: {}", command.userEmail());

        // 1. Buscar usuario
        var userOptional = userRepositoryPort.findByEmail(command.userEmail());
        if (userOptional.isEmpty()) {
            log.warn("Email not found (security measure): {}", command.userEmail());
            return;
        }
        var user = userOptional.get();

        // 2. Obtener plantilla
        EmailTemplate template = emailTemplateRepositoryPort
                .findByEmailType(EmailType.VERIFICATION_CODE)
                .orElseGet(EmailTemplate::createDefaultPasswordRecoveryTemplate);

        // 3. Preparar variables
        Map<String, String> templateVariables = prepareTemplateVariables(user, command, template);

        // 4. Procesar template - ¡CAMBIAR EL TIPO!
        TemplateProcessorService.ProcessedEmail processedEmail = templateProcessorService
                .processEmailTemplate(template, templateVariables);

        // 5. Crear notificación de email
        EmailNotification emailNotification = EmailNotification.builder()
                .user(user)
                .emailType(EmailType.VERIFICATION_CODE)
                .subject(processedEmail.getSubject())      // Cambiado
                .emailBody(processedEmail.getBody())       // Cambiado
                .timestamp(LocalDateTime.now())
                .sendStatus(EmailSendStatus.PENDING)
                .build();

        // 6. Enviar email
        emailNotificationPort.sendEmail(emailNotification);

        log.info("Password recovery email sent for React integration: {}", command.userEmail());
    }

    private Map<String, String> prepareTemplateVariables(
            edu.dosw.rideci.domain.model.User user,
            PasswordRecoveryCommand command,
            EmailTemplate template
    ) {
        Map<String, String> variables = new java.util.HashMap<>(command.additionalData());

        // Variables básicas
        variables.put("USER_NAME", user.getName() != null ? user.getName() : "Usuario");
        variables.put("USER_EMAIL", user.getEmail());
        variables.put("VERIFICATION_CODE", command.verificationCode());
        variables.put("EXPIRY_MINUTES", command.additionalData().getOrDefault("expiryMinutes", "15"));
        variables.put("APP_NAME", "RideCI");

        // Variables específicas
        String actionLink = template.generateFrontendLink(command.verificationCode());
        if (actionLink != null) {
            variables.put("ACTION_LINK", actionLink);
            variables.put("FRONTEND_URL", template.getFrontendActionUrl());
        }

        return variables;
    }
}