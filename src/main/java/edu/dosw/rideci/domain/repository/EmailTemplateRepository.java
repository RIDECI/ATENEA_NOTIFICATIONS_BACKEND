package edu.dosw.rideci.domain.repository;

import edu.dosw.rideci.domain.model.EmailTemplate;
import edu.dosw.rideci.domain.model.Enum.EmailType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmailTemplateRepository extends MongoRepository<EmailTemplate, String> {

    Optional<EmailTemplate> findByEmailTypeAndIsActiveTrue(EmailType emailType);

    List<EmailTemplate> findByIsActiveTrue();

    List<EmailTemplate> findByCategoryAndIsActiveTrue(String category);

    @Query("{ 'emailType': ?0, 'isActive': true }")
    Optional<EmailTemplate> findActiveByEmailType(EmailType emailType);

    boolean existsByNameAndIsActiveTrue(String name);
}