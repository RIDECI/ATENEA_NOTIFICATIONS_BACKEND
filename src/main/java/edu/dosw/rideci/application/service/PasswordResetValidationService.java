package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.out.PasswordResetCodeRepositoryPort;
import edu.dosw.rideci.domain.model.PasswordResetCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetValidationService {

    private final PasswordResetCodeRepositoryPort passwordResetCodeRepositoryPort;

    public boolean validateResetCode(String email, String code) {
        return passwordResetCodeRepositoryPort
                .findByEmailAndCode(email, code)
                .map(PasswordResetCode::isValid)
                .orElse(false);
    }

    public void invalidateResetCode(String email, String code) {
        passwordResetCodeRepositoryPort
                .findByEmailAndCode(email, code)
                .ifPresent(resetCode -> {
                    resetCode.setUsed(true);
                    passwordResetCodeRepositoryPort.save(resetCode);
                });
    }
}