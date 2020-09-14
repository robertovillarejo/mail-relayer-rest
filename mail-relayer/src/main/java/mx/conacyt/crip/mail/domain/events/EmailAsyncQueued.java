package mx.conacyt.crip.mail.domain.events;

import org.simplejavamail.api.email.Email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Evento que indica que se ha encolado un {@code Email} en la cola de trabajo.
 */
@Getter
@RequiredArgsConstructor
public class EmailAsyncQueued {

    /**
     * El email encolado.
     */
    private final Email email;

    /**
     * El nombre del usuario quien realizó la petición.
     */
    private final String username;
}
