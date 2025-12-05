package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.Enum.Category;
import edu.dosw.rideci.domain.model.Enum.UserProfile;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
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

    public User(String id, String name, String email, UserProfile profile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.appNotifications = new ArrayList<>();
        this.emailNotifications = new ArrayList<>();
    }

    public void addEmailNotification(EmailNotification notification) {
        if (this.emailNotifications == null) {
            this.emailNotifications = new ArrayList<>();
        }
        this.emailNotifications.add(notification);
    }

    public List<AppNotification> getNotificationsByProfileAndCategory(UserProfile profile, Category category) {
        List<AppNotification> filtered = new ArrayList<>();
        if (this.appNotifications != null) {
            for (AppNotification notification : this.appNotifications) {
                if (notification.getCategory() == category) {
                    filtered.add(notification);
                }
            }
        }
        return filtered;
    }
}
