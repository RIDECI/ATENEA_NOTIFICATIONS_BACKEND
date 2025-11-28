package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.InAppNotification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepositoryPort {

    InAppNotification save(InAppNotification notification);

    Optional<InAppNotification> findById(UUID id);

    List<InAppNotification> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
