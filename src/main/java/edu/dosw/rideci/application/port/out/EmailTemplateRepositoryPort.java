package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.EmailTemplate;
import edu.dosw.rideci.domain.model.Enum.EmailType;

import java.util.List;
import java.util.Optional;

public interface EmailTemplateRepositoryPort {

    /**
     * Busca una plantilla por su tipo de email
     *
     * @param emailType Tipo de email (ej: VERIFICATION_CODE, TRIP_CONFIRMATION, etc.)
     * @return Plantilla si existe
     */
    Optional<EmailTemplate> findByEmailType(EmailType emailType);

    /**
     * Busca todas las plantillas activas
     *
     * @return Lista de plantillas activas
     */
    List<EmailTemplate> findAllActive();

    /**
     * Guarda o actualiza una plantilla
     *
     * @param template Plantilla a guardar
     * @return Plantilla guardada
     */
    EmailTemplate save(EmailTemplate template);

    /**
     * Elimina una plantilla por su ID
     *
     * @param id ID de la plantilla
     */
    void deleteById(String id);

    /**
     * Busca plantillas por nombre (búsqueda parcial)
     *
     * @param name Nombre o parte del nombre a buscar
     * @return Lista de plantillas que coinciden
     */
    List<EmailTemplate> findByNameContaining(String name);

    /**
     * Activa o desactiva una plantilla
     *
     * @param id ID de la plantilla
     * @param active Estado a establecer
     */
    void setTemplateActive(String id, boolean active);
}