package edu.dosw.rideci.infrastructure.persistance.Repository;

import edu.dosw.rideci.infrastructure.persistance.Entity.EmailNotificationEntity;
import edu.dosw.rideci.domain.model.Enum.EmailSendStatus;
import edu.dosw.rideci.domain.model.Enum.EmailType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmailNotificationMongoRepository extends MongoRepository<EmailNotificationEntity, String> {
    List<EmailNotificationEntity> findByUserId(String userId);
    List<EmailNotificationEntity> findBySendStatus(EmailSendStatus sendStatus);
    List<EmailNotificationEntity> findByEmailType(EmailType emailType);
    List<EmailNotificationEntity> findByUserIdAndSendStatus(String userId, EmailSendStatus sendStatus);
    List<EmailNotificationEntity> findTop5ByUserIdOrderByTimestampDesc(String userId);
}