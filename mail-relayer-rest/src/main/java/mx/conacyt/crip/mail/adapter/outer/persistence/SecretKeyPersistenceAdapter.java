package mx.conacyt.crip.mail.adapter.outer.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import mx.conacyt.crip.mail.application.port.out.LoadSecretKeyPort;
import mx.conacyt.crip.mail.application.port.out.SaveSecretKeyPort;
import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.domain.SecretKeyMongoEntity;
import mx.conacyt.crip.mail.domain.User;
import mx.conacyt.crip.mail.domain.UserMongoEntity;
import mx.conacyt.crip.mail.repository.SecretKeyRepository;
import mx.conacyt.crip.mail.repository.UserRepository;

@Component
public class SecretKeyPersistenceAdapter implements SaveSecretKeyPort, LoadSecretKeyPort {

    private final SecretKeyRepository secretKeyRepository;
    private final UserRepository userRepository;

    public SecretKeyPersistenceAdapter(SecretKeyRepository secretKeyRepository, UserRepository userRepository) {
        this.secretKeyRepository = secretKeyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveSecretKey(SecretKey secretKey, User user) {
        UserMongoEntity uMongoEntity = userRepository.findById(user.getName()).orElseThrow();
        secretKeyRepository.save(new SecretKeyMongoEntity().content(secretKey.getContent()).user(uMongoEntity));
    }

    @Override
    public Optional<SecretKey> loadSecretKey(String username) {
        Optional<SecretKeyMongoEntity> maybeSecretKey = secretKeyRepository.findByUserName(username);
        if (maybeSecretKey.isPresent()) {
            return Optional.of(SecretKey.of(maybeSecretKey.get().getContent()));
        }
        return Optional.empty();
    }

}
