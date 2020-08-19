package mx.conacyt.crip.mail.domain;

import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Un usuario.
 */
@RequiredArgsConstructor
public class User {

    @Getter
    private final String name;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
