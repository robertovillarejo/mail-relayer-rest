package mx.conacyt.crip.mail.application.port.out;

import mx.conacyt.crip.mail.domain.User;

/**
 * Puerto para guardar un usuario.
 */
public interface CreateUserPort {

    /**
     * Crea un usuario con el username.
     *
     * @param username el nombre del usuario.
     * @return el usuario creado.
     */
    User createUser(String username);

}
