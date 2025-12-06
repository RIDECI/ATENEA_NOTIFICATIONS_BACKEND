package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.PasswordResetCode;

import java.util.Optional;

public interface PasswordResetCodeRepositoryPort {
    PasswordResetCode save(PasswordResetCode resetCode);
    Optional<PasswordResetCode> findByEmailAndCode(String email, String code);
    void deleteByEmail(String email);
    void deleteExpiredCodes();
}