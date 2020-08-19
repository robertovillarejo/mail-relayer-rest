package mx.conacyt.crip.mail.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mx.conacyt.crip.mail.domain.UserMongoEntity;

/**
 * Spring Data MongoDB repository for the {@link UserMongoEntity} entity.
 */
@Repository
public interface UserRepository extends MongoRepository<UserMongoEntity, String> {

    Optional<UserMongoEntity> findByName(String name);

    boolean existsByName(String name);

}
