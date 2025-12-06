package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.service.PasswordResetEventPublisher;
import edu.dosw.rideci.infrastructure.controller.dto.Request.InitiatePasswordResetRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Request.VerifyResetCodeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Password Reset", description = "Endpoints para recuperación de contraseña")
public class PasswordResetController {

    private final PasswordResetEventPublisher passwordResetEventPublisher;

    @Operation(summary = "Iniciar recuperación de contraseña")
    @PostMapping("/password-reset/initiate")
    public ResponseEntity<?> initiatePasswordReset(@Valid @RequestBody InitiatePasswordResetRequest request) {
        try {
            // 1. Generar código de recuperación
            String resetCode = generateResetCode();

            // 2. Publicar evento (el listener se encargará del correo)
            passwordResetEventPublisher.publishPasswordResetEvent(
                    request.getEmail(),
                    resetCode,
                    15 // 15 minutos de expiración
            );

            // 3. Registrar en base de datos (opcional, dependiendo de tu flujo)

            return ResponseEntity.ok(Map.of(
                    "message", "Si el email existe en nuestro sistema, recibirás un código de recuperación",
                    "status", "success"
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error procesando la solicitud"));
        }
    }

    @Operation(summary = "Verificar código de recuperación")
    @PostMapping("/password-reset/verify")
    public ResponseEntity<?> verifyResetCode(@Valid @RequestBody VerifyResetCodeRequest request) {
        // Lógica para verificar el código
        // Esto debería consultar tu base de datos donde almacenas los códigos

        return ResponseEntity.ok(Map.of(
                "valid", true,
                "message", "Código válido"
        ));
    }

    private String generateResetCode() {
        // Genera un código de 6 dígitos
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}