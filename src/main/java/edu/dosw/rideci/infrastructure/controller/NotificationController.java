package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.service.InAppNotificationApplicationService;
import edu.dosw.rideci.domain.model.InAppNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final InAppNotificationApplicationService notificationService;

    @PostMapping
    public ResponseEntity<InAppNotification> create(@RequestBody InAppNotification request) {
        InAppNotification created = notificationService.createNotification(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<InAppNotification>> list() {
        return ResponseEntity.ok(notificationService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InAppNotification> get(@PathVariable String id) {
        return ResponseEntity.ok(notificationService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InAppNotification> update(@PathVariable String id,
                                                    @RequestBody InAppNotification update) {
        return ResponseEntity.ok(notificationService.update(id, update));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<InAppNotification> markAsRead(@PathVariable String id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<InAppNotification> cancel(@PathVariable String id) {
        return ResponseEntity.ok(notificationService.cancel(id));
    }

    @PostMapping("/{id}/retry")
    public ResponseEntity<InAppNotification> retry(@PathVariable String id) {
        return ResponseEntity.ok(notificationService.retry(id));
    }

    @GetMapping("/metrics")
    public ResponseEntity<?> metrics() {
        return ResponseEntity.ok(
                java.util.Map.of(
                        "totalNotifications", notificationService.count(),
                        "module", notificationService.getModuleName()
                )
        );
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<Void> send(@PathVariable String id) {
        // TODO: aquí podrías publicar un evento o mandar email explícito
        return ResponseEntity.accepted().build();
    }
}
