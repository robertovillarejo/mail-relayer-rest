package mx.conacyt.crip.mail.domain.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mx.conacyt.crip.mail.domain.User;

/**
 * Evento de {@link User} creado.
 */
@Getter
@RequiredArgsConstructor
public class UserCreated {

    private final User user;

}
