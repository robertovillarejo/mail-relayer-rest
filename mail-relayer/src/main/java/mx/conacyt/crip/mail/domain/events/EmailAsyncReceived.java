package mx.conacyt.crip.mail.domain.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mx.conacyt.crip.mail.domain.Mail;

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
    private final Mail mail;

    /**
     * El nombre de usuario quien realizó la petición.
     */
    private final String username;

}
