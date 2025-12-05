package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.Enum.Category;
import edu.dosw.rideci.domain.model.Enum.MessageType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document(collection = "app_notifications")
public class AppNotification {
    @Id private String id;
    private User user;
    private MessageType messageType;
    private String message;
    private LocalDateTime timestamp;
    private Category category;
    private boolean read;
    public String getSummary(){ return message.length()>40? message.substring(0,40)+"..." : message; }
    public void markAsRead(){ this.read = true; }
}