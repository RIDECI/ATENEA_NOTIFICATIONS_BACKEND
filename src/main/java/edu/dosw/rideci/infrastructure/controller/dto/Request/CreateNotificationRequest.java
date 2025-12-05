package edu.dosw.rideci.infrastructure.controller.dto.Request;

import edu.dosw.rideci.domain.model.Enum.Category;
import edu.dosw.rideci.domain.model.Enum.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateNotificationRequest {


    @NotNull
    private UUID userId;

    @NotNull
    private MessageType eventType;
    @NotNull
    private Category category;

    @NotBlank
    private String message;


    private List<String> targetUserIds;

    private String priorityLevel;


}
