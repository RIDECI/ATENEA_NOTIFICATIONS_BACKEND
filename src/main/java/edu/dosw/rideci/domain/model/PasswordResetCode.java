package edu.dosw.rideci.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "password_reset_codes")
public class PasswordResetCode {
    @Id
    private String id;
    private String email;
    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean used;

    public boolean isValid() {
        return !used && LocalDateTime.now().isBefore(expiresAt);
    }
}