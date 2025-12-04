package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findById(String id);
    List<User> findAll();
    List<User> findAllByIds(List<String> ids);
    User save(User user);
    Optional<User> findByEmail(String email);
    List<User> findByProfile(String profile);
    boolean existsById(String id);
}