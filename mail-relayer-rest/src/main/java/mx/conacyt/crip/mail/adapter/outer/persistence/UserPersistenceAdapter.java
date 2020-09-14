package mx.conacyt.crip.mail.adapter.outer.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import mx.conacyt.crip.mail.application.port.out.CreateUserPort;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.domain.SecretKeyMongoEntity;
import mx.conacyt.crip.mail.domain.User;
import mx.conacyt.crip.mail.domain.UserMongoEntity;
import mx.conacyt.crip.mail.repository.SecretKeyRepository;
import mx.conacyt.crip.mail.repository.UserRepository;

@Component
public class UserPersistenceAdapter implements LoadUserPort, CreateUserPort {

    private final UserRepository userRepository;
    private final SecretKeyRepository secretKeyRepository;

    public UserPersistenceAdapter(UserRepository userRepository, SecretKeyRepository secretKeyRepository) {
        this.userRepository = userRepository;
        this.secretKeyRepository = secretKeyRepository;
    }

    @Override
    public User createUser(String username, String msgIdSuffix) {
        UserMongoEntity user = userRepository.save(new UserMongoEntity().name(username).messageIdSuffix(msgIdSuffix));
        return new User(user.getName(), user.getMessageIdSuffix());
    }

    @Override
    public boolean existsUser(String name) {
        return userRepository.existsByName(name);
    }

    @Override
    public Optional<User> loadUser(String name) {
        Optional<UserMongoEntity> maybeUser = userRepository.findByName(name);
        if (maybeUser.isPresent()) {
            UserMongoEntity user = maybeUser.get();
            return Optional.of(new User(user.getName(), user.getMessageIdSuffix()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> loadUserBySecretKey(SecretKey secretKey) {
        Optional<SecretKeyMongoEntity> maybeSk = secretKeyRepository.findByContent(secretKey.getContent());
        if (maybeSk.isPresent()) {
            UserMongoEntity user = maybeSk.get().getUser();
            return Optional.of(new User(user.getName(), user.getMessageIdSuffix()));
        }
        return Optional.empty();
    }

}
