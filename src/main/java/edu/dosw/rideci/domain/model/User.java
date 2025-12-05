package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.Enum.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;
    private String email;
    private UserProfile profile;

    private List<AppNotification> appNotifications;
    private List<EmailNotification> emailNotifications;

    public void addAppNotification(AppNotification notification) {
        if (this.appNotifications != null) {
            this.appNotifications.add(notification);
        }
    }

    public void addEmailNotification(EmailNotification notification) {
        if (this.emailNotifications != null) {
            this.emailNotifications.add(notification);
        }
    }
}
