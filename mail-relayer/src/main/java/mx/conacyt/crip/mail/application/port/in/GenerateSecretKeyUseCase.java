package mx.conacyt.crip.mail.application.port.in;

import java.util.Objects;

import lombok.Getter;
import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.domain.User;
import mx.conacyt.crip.mail.domain.exception.UserNotExists;

/**
 * El caso de uso de generar una {@link SecretKey}.
 *
 */
public interface GenerateSecretKeyUseCase {

    /**
     * Genera una {@link SecretKey} y la asocia al {@link User} con username.
     *
     * @param username el nombre del usuario.
     * @throws UserNotExists si no existe usuario con username.
     * @return una nueva secretKey.
     */
    SecretKey generateSecretKey(String username);

    /**
     * El comando para generar una {@link SecretKey}.
     */
    @Getter
    class GenerateSecretKeyCommand {

        private final String username;

        public GenerateSecretKeyCommand(String username) {
            Objects.requireNonNull(username);
            this.username = username;
        }

    }

}
