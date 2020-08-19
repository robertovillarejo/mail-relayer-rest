package mx.conacyt.crip.mail.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mx.conacyt.crip.mail.domain.EmailMongoEntity;

@Repository
public interface EmailRepository extends MongoRepository<EmailMongoEntity, String> {

}
