package edu.dosw.rideci.infrastructure.persistance.Entity;

import edu.dosw.rideci.domain.model.Enum.UserProfile;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class UserEntity {

    @Id
    private String id;

    private String name;

    private String email;

    private UserProfile profile;

    // Referencias por ID (evita documentos anidados grandes)
    @Field("app_notification_ids")
    private List<String> appNotificationIds;

    @Field("email_notification_ids")
    private List<String> emailNotificationIds;

    // Campos de auditoría
    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}