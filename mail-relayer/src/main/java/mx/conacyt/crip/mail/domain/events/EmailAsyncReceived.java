package mx.conacyt.crip.mail.domain.events;

import org.simplejavamail.api.email.Email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Evento que indica que se ha recibido la petición de enviar un email de forma
 * asíncrona.
 */
@Getter
@RequiredArgsConstructor
public class EmailAsyncReceived {

    /**
     * El email recibido con un Message-ID generado.
     */
    private final Email email;

    /**
     * El nombre de usuario quien realizó la petición.
     */
    private final String username;

}
