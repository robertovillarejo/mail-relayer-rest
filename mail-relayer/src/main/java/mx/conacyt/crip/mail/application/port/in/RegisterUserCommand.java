package mx.conacyt.crip.mail.application.port.in;

import java.util.Objects;

import lombok.Getter;
import mx.conacyt.crip.mail.domain.User;

/**
 * El comando para registrar un {@link User}.
 */
@Getter
public class RegisterUserCommand {

    private final String name;
    private final String msgIdSuffix;

    /**
     * Crea un nuevo comando para registrar un usuario.
     *
     * @param name        el nombre de usuario.
     * @param msgIdSuffix el sufijo para usar en los <code>Message-ID</code> de los
     *                    email a enviar de forma síncrona.
     */
    public RegisterUserCommand(String name, String msgIdSuffix) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(msgIdSuffix);
        this.name = name;
        this.msgIdSuffix = msgIdSuffix;
    }

}
