package mx.conacyt.crip.mail.application.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import mx.conacyt.crip.mail.application.port.out.CreateUserPort;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.domain.User;

/**
 * Adaptador de persistencia en memoria para {@link User}.
 */
class UserPersistenceAdapterInMemory implements LoadUserPort, CreateUserPort {

    private Map<String, User> users = new HashMap<>();

    @Override
    public User createUser(String username, String msgIdSuffix) {
        User user = new User(username, msgIdSuffix);
        users.put(username, user);
        return user;
    }

    @Override
    public boolean existsUser(String name) {
        return users.values().stream().filter(u -> name.equals(u.getName())).findFirst().isPresent();
    }

    @Override
    public Optional<User> loadUser(String id) {
        return Optional.of(users.get(id));
    }

    @Override
    public Optional<User> loadUserBySecretKey(SecretKey secretKey) {
        return Optional.empty();
    }

}
