package edu.dosw.rideci.application.port.in;

import java.util.Map;

public interface SendPasswordRecoveryEmailUseCase {

    /**
     * Envía un correo de recuperación de contraseña con el código de verificación
     *
     * @param command Comando con la información necesaria para enviar el correo
     */
    void sendPasswordRecoveryEmail(PasswordRecoveryCommand command);

    /**
     * Comando para la recuperación de contraseña
     *
     * @param userEmail Email del usuario que solicita la recuperación
     * @param verificationCode Código de verificación
     * @param additionalData Datos adicionales para la plantilla del correo
     */
    record PasswordRecoveryCommand(
            String userEmail,
            String verificationCode,
            Map<String, String> additionalData
    ) {}
}