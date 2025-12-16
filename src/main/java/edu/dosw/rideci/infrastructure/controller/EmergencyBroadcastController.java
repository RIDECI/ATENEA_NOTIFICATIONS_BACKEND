package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.CreateEmergencyBroadcastUseCase;
import edu.dosw.rideci.domain.model.Enum.BroadcastType;
import edu.dosw.rideci.infrastructure.controller.dto.Request.EmergencyBroadcastRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/broadcast")
@RequiredArgsConstructor
public class EmergencyBroadcastController {

    private final CreateEmergencyBroadcastUseCase createEmergencyBroadcastUseCase;

    @PostMapping
    public ResponseEntity<?> createEmergencyBroadcast(
            @RequestBody EmergencyBroadcastRequest request
    ) {
        try {

            if (request.emergencyMessage() == null || request.emergencyMessage().isBlank()) {
                return ResponseEntity.badRequest().body("emergencyMessage es obligatorio");
            }


            if (request.broadcastType() == null || request.broadcastType().isBlank()) {
                return ResponseEntity.badRequest()
                        .body("broadcastType es obligatorio");
            }

            BroadcastType broadcastType;
            try {
                broadcastType = BroadcastType.valueOf(request.broadcastType().toUpperCase());
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest()
                        .body("broadcastType inválido: " + request.broadcastType());
            }


            CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command =
                    new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                            request.emergencyMessage(),
                            broadcastType,
                            request.targetUserIds(),
                            request.priorityLevel()
                    );


            var notifications = createEmergencyBroadcastUseCase.createEmergencyBroadcast(command);

            log.info(
                    "Broadcast creado. Tipo={}, mensaje='{}', destinatarios={}",
                    broadcastType,
                    request.emergencyMessage(),
                    request.targetUserIds() != null ? request.targetUserIds().size() : "ALL"
            );

            return ResponseEntity.ok(
                    "Broadcast creado. Notificaciones generadas: " + notifications.size()
            );

        } catch (Exception e) {
            log.error("Error al crear broadcast de emergencia: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Error interno al crear broadcast de emergencia");
        }
    }
}
