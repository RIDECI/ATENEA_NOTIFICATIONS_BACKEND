package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.AppNotification;
import edu.dosw.rideci.domain.model.Enum.Category;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppNotificationRepositoryPort {
    AppNotification save(AppNotification notification);
    Optional<AppNotification> findById(String id);
    List<AppNotification> findAll();
    void deleteById(String id);
    List<AppNotification> findByUserId(String userId);
    List<AppNotification> findByCategory(Category category);
    List<AppNotification> findByUserIdAndCategory(String userId, Category category);
    List<AppNotification> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<AppNotification> findByUserIdAndTimestampBetween(String userId, LocalDateTime start, LocalDateTime end);
    List<AppNotification> findByCategoryAndTimestampBetween(Category category, LocalDateTime start, LocalDateTime end);
    List<AppNotification> findTopNByOrderByTimestampDesc(int limit);
    List<AppNotification> findTopNByUserIdOrderByTimestampDesc(String userId, int limit);
}
