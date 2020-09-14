package mx.conacyt.crip.mail.application.port.in;

import java.util.Objects;

import org.simplejavamail.api.email.Email;

import lombok.Getter;

/**
 * El comando de enviar {@link Email}.
 */
@Getter
public class SendMailCommand {

    private final Email email;
    private final boolean async;
    private final String username;

    /**
     * Crea un nuevo comando de enviar email.
     *
     * @param email    el email a enviar.
     * @param username el nombre del usuario quien envía.
     * @param async    false si síncrono | true si es asíncrono
     */
    public SendMailCommand(final Email email, String username, final boolean async) {
        Objects.requireNonNull(email.getFromRecipient());
        Objects.requireNonNull(username);
        Objects.requireNonNull(email.getRecipients());
        this.email = email;
        this.username = username;
        this.async = async;
    }

}
