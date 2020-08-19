package mx.conacyt.crip.mail.application.port.out;

import org.simplejavamail.api.email.Email;

/**
 * Puerto para guardar un {@link Email}.
 */
public interface SaveEmailPort {

    /**
     * Guarda el email enviado por el userId.
     *
     * @param email    el email enviado.
     * @param username el nombre del usuario que envió el email.
     */
    void saveEmail(Email email, String username);

}
