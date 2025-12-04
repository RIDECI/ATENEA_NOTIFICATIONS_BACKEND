package edu.dosw.rideci.infrastructure.persistance.Entity;

import edu.dosw.rideci.domain.model.Enum.EmailSendStatus;
import edu.dosw.rideci.domain.model.Enum.EmailType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "email_notifications")
public class EmailNotificationEntity {

    @Id
    private String id;

    @Field("user_id")
    private String userId;  // Solo ID

    @Field("email_type")
    private EmailType emailType;

    private String content;

    private LocalDateTime timestamp;

    @Field("send_status")
    private EmailSendStatus sendStatus;

    private String error;

    // Campos de auditoría
    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}