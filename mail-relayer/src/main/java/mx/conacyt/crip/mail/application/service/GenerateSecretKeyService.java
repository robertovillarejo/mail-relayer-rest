package mx.conacyt.crip.mail.application.service;

import com.google.common.eventbus.Subscribe;

import java.util.Optional;
import mx.conacyt.crip.mail.application.port.in.GenerateSecretKeyUseCase;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.application.port.out.SaveSecretKeyPort;
import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.domain.User;
import mx.conacyt.crip.mail.domain.events.UserCreated;
import mx.conacyt.crip.mail.domain.exception.UserNotExists;

/**
 * Implementación del caso de uso de generar una secretKey.
 */
class GenerateSecretKeyService implements GenerateSecretKeyUseCase {

    private final SaveSecretKeyPort saveSecretKeyPort;
    private final LoadUserPort loadUserPort;

    /**
     * Crea el servicio.
     *
     * @param saveSecretKeyPort
     * @param loadUserPort
     */
    GenerateSecretKeyService(SaveSecretKeyPort saveSecretKeyPort, LoadUserPort loadUserPort) {
        DomainEventBus.EVENT_BUS.register(this);
        this.saveSecretKeyPort = saveSecretKeyPort;
        this.loadUserPort = loadUserPort;
    }

    @Override
    public SecretKey generateSecretKey(String username) {
        Optional<User> maybeUser = loadUserPort.loadUser(username);
        if (!maybeUser.isPresent()) {
            throw new UserNotExists();
        }
        SecretKey secretKey = SecretKey.generate();
        saveSecretKeyPort.saveSecretKey(secretKey, maybeUser.get());
        return secretKey;
    }

    @Subscribe
    public void onUserCreated(UserCreated event) {
        generateSecretKey(event.getUser().getName());
    }
}
