package edu.dosw.rideci.infrastructure.controller.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyResetCodeRequest {
    @NotBlank(message = "Email es requerido")
    private String email;

    @NotBlank(message = "Código es requerido")
    private String code;

    @NotBlank(message = "Nueva contraseña es requerida")
    private String newPassword;
}