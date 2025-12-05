package edu.dosw.rideci.infrastructure.controller.dto.Request;

import edu.dosw.rideci.domain.model.Enum.EmailType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;

/**
 * Request DTO to send an email notification from the API.
 */


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationRequest {


    @NotNull
    private String userId;

    private EmailType emailType;
    private Map<String, String> templateData;

    @NotBlank
    @Email
    private String toEmail;


    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}
