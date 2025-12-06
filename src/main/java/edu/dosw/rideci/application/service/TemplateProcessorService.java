package edu.dosw.rideci.application.service;

import edu.dosw.rideci.domain.model.EmailTemplate;
import edu.dosw.rideci.domain.model.Enum.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TemplateProcessorService {

    /**
     * Procesa cualquier template con sus variables
     */
    public String processTemplate(String templateContent, Map<String, String> variables) {
        if (templateContent == null || variables == null) {
            return templateContent;
        }

        String processed = templateContent;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            processed = processed.replace(placeholder, entry.getValue() != null ? entry.getValue() : "");
        }
        return processed;
    }

    /**
     * Procesa un template de email completo
     */
    public ProcessedEmail processEmailTemplate(
            EmailTemplate template,
            Map<String, String> variables
    ) {
        // Validar que el template esté activo
        if (!template.isActive()) {
            throw new IllegalArgumentException("Template inactivo: " + template.getName());
        }

        // Validar variables requeridas
        if (!template.validateVariables(variables)) {
            throw new IllegalArgumentException(
                    "Faltan variables requeridas para el template: " +
                            template.getRequiredVariables()
            );
        }

        // Añadir variables especiales
        Map<String, String> enhancedVariables = enhanceVariables(template, variables);

        // Procesar asunto y cuerpo
        String subject = processTemplate(template.getSubjectTemplate(), enhancedVariables);
        String body = processTemplate(template.getBodyTemplate(), enhancedVariables);

        return ProcessedEmail.builder()
                .subject(subject)
                .body(body)
                .actionLink(template.generateFrontendLink(enhancedVariables.get("VERIFICATION_CODE")))
                .callToAction(template.getCallToActionText())
                .userInstructions(template.getUserInstructions())
                .emailType(template.getEmailType())
                .build();
    }

    /**
     * Procesa un template específico por tipo de email
     */
    public ProcessedEmail processByEmailType(
            EmailType emailType,
            Map<String, String> variables,
            List<EmailTemplate> availableTemplates
    ) {
        EmailTemplate template = availableTemplates.stream()
                .filter(t -> t.getEmailType() == emailType && t.isActive())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay template activo para el tipo: " + emailType
                ));

        return processEmailTemplate(template, variables);
    }

    /**
     * Enriquecer variables con valores específicos del template
     */
    private Map<String, String> enhanceVariables(EmailTemplate template, Map<String, String> variables) {
        Map<String, String> enhanced = new HashMap<>(variables);

        // Variables globales
        enhanced.putIfAbsent("APP_NAME", "RideCI");

        // Variables específicas para enlaces React
        String code = variables.get("VERIFICATION_CODE");
        if (code != null && template.getFrontendActionUrl() != null) {
            String actionLink = template.generateFrontendLink(code);
            enhanced.put("ACTION_LINK", actionLink);
            enhanced.put("FRONTEND_URL", template.getFrontendActionUrl());
        }

        // Metadata del template
        if (template.getMetadata() != null) {
            template.getMetadata().forEach((key, value) ->
                    enhanced.putIfAbsent("META_" + key.toUpperCase(), value)
            );
        }

        return enhanced;
    }

    /**
     * Clase DTO para el email procesado
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProcessedEmail {
        private String subject;
        private String body;
        private String actionLink;
        private String callToAction;
        private String userInstructions;
        private EmailType emailType;
    }
}