package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.Enum.EmailType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "email_templates")
public class EmailTemplate {

    @Id
    private String id;

    private String name;

    private EmailType emailType;

    private String subjectTemplate;

    /**
     * Template en texto plano para el cuerpo del email
     * Variables entre {llaves} que serán reemplazadas
     */
    private String bodyTemplate;

    private List<String> availableVariables;

    private String description;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private Integer version = 1;

    /**
     * Para React: URL del frontend donde se maneja la acción
     * Ej: "https://app.rideci.com/reset-password"
     */
    private String frontendActionUrl;

    /**
     * Para React: Nombre del parámetro para el código
     * Ej: "code" para https://app.rideci.com/reset-password?code={VERIFICATION_CODE}
     */
    private String codeParameterName;

    /**
     * Para React: Texto del botón/llamada a la acción
     */
    private String callToActionText;

    /**
     * Para React: Instrucciones adicionales para el usuario
     */
    private String userInstructions;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String category;

    private Map<String, String> metadata;

    /**
     * Genera un link completo para React
     */
    public String generateFrontendLink(String code) {
        if (frontendActionUrl == null || codeParameterName == null) {
            return null;
        }
        return frontendActionUrl + "?" + codeParameterName + "=" + code;
    }

    /**
     * Valida que la plantilla tenga todas las variables necesarias
     */
    public boolean validateVariables(Map<String, String> variables) {
        if (availableVariables == null || availableVariables.isEmpty()) {
            return true;
        }

        return availableVariables.stream()
                .filter(var -> !var.startsWith("OPTIONAL_"))
                .allMatch(variables::containsKey);
    }

    /**
     * Obtiene la lista de variables requeridas
     */
    public List<String> getRequiredVariables() {
        if (availableVariables == null) {
            return Collections.emptyList();
        }

        return availableVariables.stream()
                .filter(var -> !var.startsWith("OPTIONAL_"))
                .collect(Collectors.toList());
    }

    /**
     * Método para crear plantilla por defecto de recuperación de contraseña
     */
    public static EmailTemplate createDefaultPasswordRecoveryTemplate() {
        return EmailTemplate.builder()
                .name("Recuperación de Contraseña - React")
                .emailType(EmailType.VERIFICATION_CODE)
                .subjectTemplate("Recupera tu contraseña en RideCI")
                .bodyTemplate("""
                Hola {USER_NAME},
                
                Recibimos una solicitud para restablecer tu contraseña en RideCI.
                
                🗝️ **Tu código de verificación es:**
                {VERIFICATION_CODE}
                
                ⏰ **Este código expirará en:** {EXPIRY_MINUTES} minutos
                
                🌐 **Para continuar:**
                1. Ve a nuestra aplicación: {FRONTEND_URL}
                2. Ingresa el código anterior
                3. Crea tu nueva contraseña
                
                🔗 **O haz clic en este enlace directo:**
                {ACTION_LINK}
                
                ¿No solicitaste este cambio? Ignora este correo.
                
                Saludos,
                El equipo de RidECI 🚗
                
                ---
                ℹ️ *Este es un correo automático, por favor no respondas.*
                """)
                .availableVariables(List.of(
                        "USER_NAME",
                        "VERIFICATION_CODE",
                        "EXPIRY_MINUTES",
                        "FRONTEND_URL",
                        "ACTION_LINK",
                        "APP_NAME"
                ))
                .description("Plantilla para recuperación de contraseña con integración React")
                .isActive(true)
                .frontendActionUrl("https://app.rideci.com/auth/reset-password")
                .codeParameterName("code")
                .callToActionText("Restablecer Contraseña")
                .userInstructions("Usa el código proporcionado en la aplicación para continuar")
                .category("SECURITY")
                .metadata(Map.of(
                        "reactComponent", "PasswordReset",
                        "expiryTime", "15",
                        "retryAttempts", "3"
                ))
                .build();
    }

    /**
     * Método para crear plantilla genérica de verificación
     */
    public static EmailTemplate createDefaultVerificationTemplate() {
        return EmailTemplate.builder()
                .name("Verificación de Email - React")
                .emailType(EmailType.VERIFICATION_CODE)
                .subjectTemplate("Verifica tu email en RideCI")
                .bodyTemplate("""
                Hola {USER_NAME},
                
                ¡Bienvenido a RideCI! 🎉
                
                Para completar tu registro, necesitamos verificar tu dirección de email.
                
                🔐 **Tu código de verificación es:**
                {VERIFICATION_CODE}
                
                🌐 **Para verificar tu email:**
                1. Abre la aplicación RideCI
                2. Ingresa el código anterior
                3. ¡Listo! Tu cuenta estará verificada
                
                🔗 **Enlace directo de verificación:**
                {ACTION_LINK}
                
                El código es válido por {EXPIRY_MINUTES} minutos.
                
                Saludos,
                El equipo de RideCI 🚗
                
                ---
                ℹ️ *Si no creaste una cuenta en RideCI, ignora este correo.*
                """)
                .availableVariables(List.of(
                        "USER_NAME",
                        "VERIFICATION_CODE",
                        "EXPIRY_MINUTES",
                        "ACTION_LINK",
                        "APP_NAME"
                ))
                .isActive(true)
                .frontendActionUrl("https://app.rideci.com/auth/verify-email")
                .codeParameterName("token")
                .callToActionText("Verificar Email")
                .category("ONBOARDING")
                .build();
    }
}