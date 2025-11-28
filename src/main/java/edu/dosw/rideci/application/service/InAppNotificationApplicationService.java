package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.CreateNotificationUseCase;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.repository.InMemoryNotificationStore;
import edu.dosw.rideci.domain.service.NotificationDomainService;
import lombok.Getter;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class InAppNotificationApplicationService implements CreateNotificationUseCase {

    private final InMemoryNotificationStore store = new InMemoryNotificationStore();
    private final NotificationDomainService domainService;

    @Getter
    private final String moduleName = "ATENEA_NOTIFICATIONS_BACKEND";

    public InAppNotificationApplicationService(NotificationDomainService domainService) {
        this.domainService = domainService;
    }

    @Override
    public InAppNotification createNotification(InAppNotification notification) {
        domainService.initializeNotification(notification);
        return store.save(notification);
    }

    public List<InAppNotification> list() {
        return store.findAll();
    }

    public InAppNotification get(String id) {
        return store.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
    }

    public InAppNotification update(String id, InAppNotification update) {
        InAppNotification existing = get(id);
        existing.setTitle(update.getTitle());
        existing.setMessage(update.getMessage());
        return store.save(existing);
    }

    public void delete(String id) {
        store.delete(id);
    }

    public InAppNotification markAsRead(String id) {
        InAppNotification notification = get(id);
        domainService.markAsRead(notification);
        return store.save(notification);
    }

    public InAppNotification cancel(String id) {
        InAppNotification notification = get(id);
        domainService.archive(notification);
        return store.save(notification);
    }

    public InAppNotification retry(String id) {
        InAppNotification notification = get(id);
        domainService.initializeNotification(notification);
        return store.save(notification);
    }

    public long count() {
        return store.count();
    }
}
