package mx.conacyt.crip.mail.application.port.out;

import mx.conacyt.crip.mail.domain.Mail;

/**
 * Puerto para guardar un {@link Mail}.
 */
public interface SaveEmailPort {

    /**
     * Guarda el email enviado por el userId.
     *
     * @param email    el email enviado.
     * @param username el nombre del usuario que envió el email.
     */
    void saveEmail(Mail email, String username);

}
