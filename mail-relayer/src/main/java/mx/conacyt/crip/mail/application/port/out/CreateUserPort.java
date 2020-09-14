package mx.conacyt.crip.mail.application.port.out;

import mx.conacyt.crip.mail.domain.User;

/**
 * Puerto para guardar un usuario.
 */
public interface CreateUserPort {

    /**
     * Crea un usuario con el username.
     *
     * @param username    el nombre del usuario.
     * @param msgIdSuffix el sufijo a utilizar en los <code>Message-ID</code> que se
     *                    envían de forma síncrona
     * @return el usuario creado.
     */
    User createUser(String username, String msgIdSuffix);

}
