package mx.conacyt.crip.mail.domain.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mx.conacyt.crip.mail.domain.Mail;

/**
 * Evento que indica que se ha encolado un {@code Email} en la cola de trabajo.
 */
@Getter
@RequiredArgsConstructor
public class EmailAsyncQueued {

    /**
     * El email encolado.
     */
    private final Mail mail;

    /**
     * El nombre del usuario quien realizó la petición.
     */
    private final String username;
}
