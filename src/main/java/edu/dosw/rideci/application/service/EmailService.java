package edu.dosw.rideci.application.service;

import edu.dosw.rideci.domain.model.EmailTemplate;
import edu.dosw.rideci.domain.model.Enum.EmailType;
import edu.dosw.rideci.domain.repository.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailTemplateRepository templateRepository;
    private final TemplateProcessorService templateProcessor;

    /**
     * Enviar email usando template por tipo
     */
    @Transactional(readOnly = true)
    public TemplateProcessorService.ProcessedEmail prepareEmail(
            EmailType emailType,
            Map<String, String> variables
    ) {
        // Buscar template activo
        EmailTemplate template = templateRepository
                .findByEmailTypeAndIsActiveTrue(emailType)
                .orElseThrow(() -> new RuntimeException(
                        "No hay template configurado para: " + emailType
                ));

        // Procesar template
        return templateProcessor.processEmailTemplate(template, variables);
    }

    /**
     * Crear template predeterminado si no existe
     */
    @Transactional
    public void ensureDefaultTemplatesExist() {
        // Verificar y crear templates predeterminados
        ensureTemplateExists(EmailTemplate.createDefaultPasswordRecoveryTemplate());
        ensureTemplateExists(EmailTemplate.createDefaultVerificationTemplate());
    }

    private void ensureTemplateExists(EmailTemplate defaultTemplate) {
        boolean exists = templateRepository.existsByNameAndIsActiveTrue(defaultTemplate.getName());
        if (!exists) {
            templateRepository.save(defaultTemplate);
        }
    }
}