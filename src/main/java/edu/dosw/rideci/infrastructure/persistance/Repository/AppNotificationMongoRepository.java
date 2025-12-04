package edu.dosw.rideci.infrastructure.persistance.Repository;

import edu.dosw.rideci.infrastructure.persistance.Entity.AppNotificationEntity;
import edu.dosw.rideci.domain.model.Enum.Category;
import edu.dosw.rideci.domain.model.Enum.MessageType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppNotificationMongoRepository extends MongoRepository<AppNotificationEntity, String> {
    List<AppNotificationEntity> findByUserId(String userId);
    List<AppNotificationEntity> findByUserIdAndReadFalse(String userId);
    List<AppNotificationEntity> findByCategory(Category category);
    List<AppNotificationEntity> findByMessageType(MessageType messageType);
    List<AppNotificationEntity> findTop10ByUserIdOrderByTimestampDesc(String userId);
    Long countByUserIdAndReadFalse(String userId);
}