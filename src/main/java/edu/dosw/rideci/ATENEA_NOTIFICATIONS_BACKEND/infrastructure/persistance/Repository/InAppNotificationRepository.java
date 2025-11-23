package edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.infrastructure.persistance.Repository;

import edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.infrastructure.persistance.Entity.InAppNotificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InAppNotificationRepository
        extends MongoRepository<InAppNotificationDocument, String> {

    List<InAppNotificationDocument> findByUserId(String userId);
}
