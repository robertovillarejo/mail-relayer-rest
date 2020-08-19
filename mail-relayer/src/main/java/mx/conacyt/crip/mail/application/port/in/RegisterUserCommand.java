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

    public RegisterUserCommand(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

}
