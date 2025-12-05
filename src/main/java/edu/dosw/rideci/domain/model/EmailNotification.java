package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.Enum.EmailSendStatus;
import edu.dosw.rideci.domain.model.Enum.EmailType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "email_notifications")
public class EmailNotification {

    @Id
    private String id;

    private User user;
    private EmailType emailType;
    private String subject;
    private String emailBody;
    private LocalDateTime timestamp;
    private EmailSendStatus sendStatus;
    private String error;

    /**
     * Retorna el contenido del email.
     */
    public String getContent() {
        return this.emailBody;
    }

    /**
     * Retorna la línea de asunto del email.
     */
    public String getSubjectLine() {
        return this.subject;
    }

    /**
     * Marca el email como enviado.
     */
    public void markAsSent() {
        this.sendStatus = EmailSendStatus.SENT;
    }

    /**
     * Marca el email como fallido e incluye el error.
     */
    public void markAsFailed(String error) {
        this.sendStatus = EmailSendStatus.FAILED;
        this.error = error;
    }
}
