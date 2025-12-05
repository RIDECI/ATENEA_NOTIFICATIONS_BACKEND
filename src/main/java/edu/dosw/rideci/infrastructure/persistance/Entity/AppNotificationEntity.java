package edu.dosw.rideci.infrastructure.persistance.Entity;

import edu.dosw.rideci.domain.model.Enum.Category;
import edu.dosw.rideci.domain.model.Enum.MessageType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "app_notifications")
public class AppNotificationEntity {

    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("message_type")
    private MessageType messageType;

    private String message;

    private LocalDateTime timestamp;

    private Category category;

    @Field("is_read")
    private boolean read;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}