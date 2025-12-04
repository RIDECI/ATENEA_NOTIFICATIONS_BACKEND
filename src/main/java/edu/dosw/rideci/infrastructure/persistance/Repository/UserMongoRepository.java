package edu.dosw.rideci.infrastructure.persistance.Repository;

import edu.dosw.rideci.infrastructure.persistance.Entity.UserEntity;
import edu.dosw.rideci.domain.model.Enum.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserMongoRepository extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByProfile(UserProfile profile);
    Boolean existsByEmail(String email);
}