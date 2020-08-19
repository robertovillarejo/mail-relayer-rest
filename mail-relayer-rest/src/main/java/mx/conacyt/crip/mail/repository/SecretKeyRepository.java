package mx.conacyt.crip.mail.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mx.conacyt.crip.mail.domain.SecretKeyMongoEntity;

@Repository
public interface SecretKeyRepository extends MongoRepository<SecretKeyMongoEntity, String> {

    Optional<SecretKeyMongoEntity> findByUserName(String username);

    Optional<SecretKeyMongoEntity> findByContent(String content);

}
