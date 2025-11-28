package edu.dosw.rideci.application.controller;

import edu.dosw.rideci.domain.service.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/internal/metrics")
@RequiredArgsConstructor
public class NotificationMetricsController {

    private final EventBus eventBus;

    @GetMapping("/event-queue-size")
    public Map<String, Integer> getEventQueueSize() {
        return Map.of("queueSize", eventBus.getQueueSize());
    }
}
