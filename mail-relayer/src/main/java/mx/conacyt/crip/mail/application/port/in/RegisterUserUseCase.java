package mx.conacyt.crip.mail.application.port.in;

import mx.conacyt.crip.mail.domain.User;
import mx.conacyt.crip.mail.domain.exception.UsernameAlreadyExists;

/**
 * El caso de usuario de registrar un {@link User}.
 */
public interface RegisterUserUseCase {

    /**
     * Registra un nuevo usuario.
     *
     * @param command el comando con la información necesaria.
     * @return el nuevo usuario creado.
     * @throws UsernameAlreadyExists si ya existe usuario con el nombre
     *                               proporcionado.
     */
    User registerUser(RegisterUserCommand command);

}
