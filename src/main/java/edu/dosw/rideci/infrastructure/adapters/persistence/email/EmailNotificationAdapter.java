package edu.dosw.rideci.infrastructure.adapters.email;

import edu.dosw.rideci.application.port.out.EmailNotificationPort;
import edu.dosw.rideci.domain.model.EmailNotification;
import edu.dosw.rideci.infrastructure.persistance.Repository.EmailNotificationMongoRepository;
import edu.dosw.rideci.infrastructure.persistance.Entity.EmailNotificationEntity;
import edu.dosw.rideci.infrastructure.persistance.Repository.mapper.EmailNotificationPersistenceMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Infrastructure adapter that implements the EmailNotificationPort.
 * It sends real emails and persists them into MongoDB.
 */
@Component
public class EmailNotificationAdapter implements EmailNotificationPort {

    private final JavaMailSender mailSender;
    private final EmailNotificationMongoRepository emailRepository;
    private final EmailNotificationPersistenceMapper mapper;

    public EmailNotificationAdapter(JavaMailSender mailSender,
                                    EmailNotificationMongoRepository emailRepository,
                                    EmailNotificationPersistenceMapper mapper) {
        this.mailSender = mailSender;
        this.emailRepository = emailRepository;
        this.mapper = mapper;
    }

    @Override
    public void sendEmail(EmailNotification emailNotification) {
        // 1. Persist in Mongo
        EmailNotificationEntity entity = mapper.toEntity(emailNotification);
        emailRepository.save(entity);

        // 2. Build and send the real email
        SimpleMailMessage message = new SimpleMailMessage();

        // TODO: ajusta estos getters a tu modelo real
        message.setTo(emailNotification.getUser().getEmail());
        message.setSubject(emailNotification.getSubject());
        message.setText(emailNotification.getContent());

        try {
            mailSender.send(message);
            emailNotification.markAsSent();
        } catch (Exception ex) {
            emailNotification.markAsFailed(ex.getMessage());
        }

        // 3. Update status in Mongo (optional but recommended)
        EmailNotificationEntity updatedEntity = mapper.toEntity(emailNotification);
        emailRepository.save(updatedEntity);
    }

    @Override
    public void sendBulkEmails(List<EmailNotification> emailNotifications) {
        emailNotifications.forEach(this::sendEmail);
    }
}
