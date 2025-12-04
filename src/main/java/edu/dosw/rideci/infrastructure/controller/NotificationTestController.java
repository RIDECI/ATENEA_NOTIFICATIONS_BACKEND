package edu.dosw.rideci.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications/test")
public class NotificationTestController {

    private final ReceiveExternalEventUseCase useCase;

    @PostMapping
    public void simulate(@RequestBody NotificationEvent event) {
        useCase.receive(event);
    }
}
