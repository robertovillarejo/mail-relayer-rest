package mx.conacyt.crip.mail.application.port.in;

import java.util.Objects;

import lombok.Getter;
import mx.conacyt.crip.mail.domain.SecretKey;

/**
 * El comando para generar una {@link SecretKey}.
 */
@Getter
public class GenerateSecretKeyCommand {

    private final String username;

    public GenerateSecretKeyCommand(String username) {
        Objects.requireNonNull(username);
        this.username = username;
    }

}
