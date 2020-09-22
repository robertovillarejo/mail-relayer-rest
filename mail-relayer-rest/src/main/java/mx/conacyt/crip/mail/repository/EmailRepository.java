package mx.conacyt.crip.mail.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mx.conacyt.crip.mail.domain.EmailMongoEntity;

@Repository
public interface EmailRepository extends MongoRepository<EmailMongoEntity, String> {

    /**
     * Obtiene un {@code EmailMongoEntity} por su id y el nombre de usuario que lo
     * envió.
     *
     * @param id       el id del email.
     * @param username el nombre del usuario quien lo envió.
     * @return el email dentro o vacío si no se encontró.
     */
    Optional<EmailMongoEntity> findByIdAndUser(String id, String username);

}
