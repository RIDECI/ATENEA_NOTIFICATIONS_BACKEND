package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.Enum.EmailSendStatus;
import edu.dosw.rideci.domain.model.Enum.EmailType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document(collection = "email_notifications")
public class EmailNotification {
    @Id private String id;
    private User user;
    private EmailType emailType;
    private String content;
    private LocalDateTime timestamp;
    private EmailSendStatus sendStatus;
    private String error;
    public String getSubject(){ return emailType.name(); }
}