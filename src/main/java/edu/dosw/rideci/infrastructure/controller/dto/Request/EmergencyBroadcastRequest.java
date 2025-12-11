package edu.dosw.rideci.infrastructure.controller.dto.Request;

import java.util.List;

public record EmergencyBroadcastRequest(
        String emergencyMessage,
        String broadcastType,
        List<String> targetUserIds,
        String priorityLevel
) {}
