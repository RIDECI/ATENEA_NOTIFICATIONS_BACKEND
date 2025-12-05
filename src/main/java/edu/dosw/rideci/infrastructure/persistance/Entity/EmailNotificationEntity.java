package edu.dosw.rideci.infrastructure.persistance.Entity;

import edu.dosw.rideci.domain.model.Enum.EmailSendStatus;
import edu.dosw.rideci.domain.model.Enum.EmailType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "email_notifications")
public class EmailNotificationEntity {


    @Id
    private String id;

    @DBRef
    private UserEntity user;

    @Field("tipo_email")
    private EmailType emailType;

    @Setter
    @Field("asunto")
    private String subject;

    @Field("contenido")
    private String emailBody;

    @Setter
    @Field("timestamp")
    private LocalDateTime timestamp;

    @Setter
    @Field("estado_envio")
    private EmailSendStatus sendStatus;

    @Setter
    @Field("error")
    private String error;

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setEmailType(EmailType emailType) {
        this.emailType = emailType; }

    public void setContenido(String contenido) {
        this.emailBody = contenido; }

}